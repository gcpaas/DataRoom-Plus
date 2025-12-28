package com.gccloud.gcpaas.core.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gccloud.starter.common.config.GlobalConfig;
import com.gccloud.starter.common.config.bean.JwtClientBind;
import com.gccloud.starter.common.config.bean.QianXingRestSso;
import com.gccloud.starter.common.constant.GlobalConst;
import com.gccloud.starter.common.entity.SysMenuEntity;
import com.gccloud.starter.common.entity.SysRoleEntity;
import com.gccloud.starter.common.entity.SysUserEntity;
import com.gccloud.starter.common.entity.SysUserRoleEntity;
import com.gccloud.starter.common.exception.GlobalException;
import com.gccloud.starter.common.json.JSON;
import com.gccloud.starter.common.module.login.cache.SysTokenCache;
import com.gccloud.starter.common.utils.HttpUtils;
import com.gccloud.starter.common.utils.JwtUtils;
import com.gccloud.starter.common.utils.UserUtils;
import com.gccloud.starter.common.vo.CurrentUserBase;
import com.gccloud.starter.core.dao.SysRoleDao;
import com.gccloud.starter.core.dao.SysUserDao;
import com.gccloud.starter.core.dao.SysUserRoleDao;
import com.gccloud.starter.core.service.*;
import com.gccloud.starter.plugins.cache.common.IStarterCache;
import com.gccloud.uc.sdk.SignUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 认证
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "gc.starter.component", name = "ShiroAuthRealm", havingValue = "ShiroAuthRealm", matchIfMissing = true)
public class ShiroAuthRealm extends AuthorizingRealm {

    @Resource
    private SysRoleDao sysRoleDao;

    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysSignatureService signatureService;

    @Resource
    private GlobalConfig globalConfig;

    @Resource
    private ISysMenuService menuService;

    @Resource
    private IStarterCache starterCache;

    @Resource
    private ISysTokenService tokenService;

    private final ReentrantLock userAutoRegisterLock = new ReentrantLock();

    @PostConstruct
    public void initTip() {
        log.info(GlobalConst.Console.LINE);
        log.info("初始化框架默认的Shiro认证逻辑");
        log.info(GlobalConst.Console.LINE);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ShiroAuthToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        CurrentUserBase user = (CurrentUserBase) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(user.getPermissions());
        info.setRoles(user.getRoleCodes());
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String tmpUsername = "";
        // 是否需要第三方系统单点登录
        QianXingRestSso thirdPartyRestSso = null;
        CurrentUserBase thirdPartyUser = null;
        if (token.getClass().equals(SignatureAuthToken.class)) {
            // 校验签名
            SignatureAuthToken signatureAuthToken = (SignatureAuthToken) token;
            HttpServletRequest request = signatureAuthToken.getRequest();
            signatureService.validateSignature(request);
            // 签名校验通过，获取用户名
            tmpUsername = request.getHeader(SignUtils.U_NAME);
        } else {
            String encryptedToken = (String) token.getPrincipal();
            if ("null".equals(encryptedToken)) {
                throw new GlobalException(GlobalConst.Response.Msg.NO_TOKEN, GlobalConst.Response.Code.NO_LOGIN);
            }
            if (StringUtils.isBlank(encryptedToken)) {
                throw new GlobalException(GlobalConst.Response.Msg.NO_TOKEN, GlobalConst.Response.Code.NO_LOGIN);
            }
            String tokenIssuer = null;
            if (encryptedToken.contains(GlobalConst.Jwt.SPLIT)) {
                // 加密的token
                tokenIssuer = encryptedToken.split(GlobalConst.Jwt.SPLIT)[0];
            } else {
                // 非加密的token
                JSONObject jwtObj = JwtUtils.parseWithOutValidate(encryptedToken);
                if (jwtObj == null || jwtObj.isNull("iss")) {
                    tokenIssuer = "unrecognized";
                } else {
                    tokenIssuer = jwtObj.getString("iss");
                }
            }
            String localIssuer = globalConfig.getJwt().getIssuer();
            if (!localIssuer.equals(tokenIssuer)) {
                // 单点登录，带着其他应用的token进来的
                List<QianXingRestSso> restSsoList = globalConfig.getRestSsoList();
                for (QianXingRestSso restSso : restSsoList) {
                    if (!restSso.getEnable()) {
                        continue;
                    }
                    if (restSso.getIssuer().equals(tokenIssuer)) {
                        thirdPartyRestSso = restSso;
                        break;
                    }
                }
                if (thirdPartyRestSso == null) {
                    // 不是本系统的token，直接抛异常
                    log.error("不是符合指定的应用token值规范，无法完成单点登录");
                    throw new GlobalException(GlobalConst.Response.Msg.NO_TOKEN, GlobalConst.Response.Code.NO_LOGIN);
                }
                boolean autoAddedUser = false;
                HashMap<String, String> header = new HashMap<>();
                header.put(thirdPartyRestSso.getTokenKey(), encryptedToken);
                header.put("Cookie", thirdPartyRestSso.getTokenKey() + "=" + encryptedToken);
                long start = System.currentTimeMillis();
                try (Response response = HttpUtils.get(thirdPartyRestSso.getCurrentUserUrl(), header)) {
                    if (!response.isSuccessful()) {
                        log.error("单点登录校验失败，url: {} 响应内容: {}", thirdPartyRestSso.getCurrentUserUrl(), response.body().string());
                        throw new GlobalException("单点登录校验失败");
                    }
                    String respBody = response.body().string();
                    log.info("单点登录校验结果，url: {} 响应内容: {} 耗时: {} 毫秒", thirdPartyRestSso.getCurrentUserUrl(), respBody, System.currentTimeMillis() - start);
                    JSONObject respObj = JSON.parseObject(respBody);
                    int code = respObj.getInt("code");
                    if (code != 200) {
                        throw new GlobalException("单点登录校验失败");
                    }
                    String username = "";
                    JSONObject respDataObj = respObj.getJSONObject("data");
                    if (!respDataObj.isNull("name")) {
                        username = respDataObj.getString("name");
                    }
                    if (StringUtils.isBlank(username)) {
                        if (!respDataObj.isNull("username")) {
                            username = respDataObj.getString("username");
                        }
                    }
                    String realName = respObj.getJSONObject("data").getString("realName");
                    // 设置当前用户、用于下面去远程获取权限后使用
                    thirdPartyUser = new CurrentUserBase();
                    if (!respDataObj.isNull("id")) {
                        thirdPartyUser.setId(respDataObj.getString("id"));
                    }
                    thirdPartyUser.setName(username);
                    if (!respDataObj.isNull("realName")) {
                        thirdPartyUser.setRealName(respDataObj.getString("realName"));
                    }
                    if (!respDataObj.isNull("tenantId")) {
                        thirdPartyUser.setTenantId(respDataObj.getString("tenantId"));
                    }
                    if (!respDataObj.isNull("orgId")) {
                        thirdPartyUser.setOrgId(respDataObj.getString("orgId"));
                    }

                    tmpUsername = username;
                    if (thirdPartyRestSso.getAutoRegister()) {
                        // 自动注册用户
                        SysUserEntity dbUser = userService.getByUserName(username);
                        if (dbUser == null) {
                            try {
                                userAutoRegisterLock.lock();
                                SysUserEntity tryGetUser = userService.getByUserName(username);
                                if (tryGetUser == null) {
                                    // 自动注册
                                    SysUserEntity tempUser = sysUserDao.selectOne(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getUsername, "admin"));
                                    SysUserEntity newUser = new SysUserEntity();
                                    BeanUtils.copyProperties(tempUser, newUser);
                                    newUser.setId(null);
                                    newUser.setUsername(username);
                                    newUser.setRealName(realName);
                                    // 3天后密码过期
                                    newUser.setPwdExpireDate(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000));
                                    // 密码随机生成
                                    newUser.setPassword(IdWorker.get32UUID());
                                    sysUserDao.insert(newUser);
                                    autoAddedUser = true;
                                    for (String roleCode : thirdPartyRestSso.getDefaultRoleCodeList()) {
                                        SysRoleEntity dbRole = sysRoleDao.selectOne(new LambdaQueryWrapper<SysRoleEntity>().eq(SysRoleEntity::getCode, roleCode));
                                        if (dbRole == null) {
                                            throw new GlobalException("为用户【" + username + "】自动分配编码为【" + roleCode + "】的角色失败，不存在该角色");
                                        }
                                        // 给用户授权角色
                                        SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
                                        sysUserRoleEntity.setUserId(newUser.getId());
                                        sysUserRoleEntity.setRoleId(dbRole.getId());
                                        sysUserRoleEntity.setCreateBy("2");
                                        sysUserRoleEntity.setCreateDate(new Date());
                                        sysUserRoleEntity.setUpdateBy("2");
                                        sysUserRoleEntity.setUpdateDate(new Date());
                                        sysUserRoleEntity.setTenantId("2");
                                        sysUserRoleDao.insert(sysUserRoleEntity);
                                        log.warn("用户 {} 首次单点登录，系统自动注册该用户并为其分配：{}(编码: {}) 角色", username, dbRole.getName(), dbRole.getCode());
                                    }
                                }
                            } finally {
                                userAutoRegisterLock.unlock();
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                    if (autoAddedUser && StringUtils.isNotBlank(tmpUsername)) {
                        // 回滚已经添加的用户
                        sysUserDao.delete(new LambdaQueryWrapper<SysUserEntity>().eq(SysUserEntity::getUsername, tmpUsername));
                    }
                    throw e;
                }
            } else {
                String accessToken = null;
                try {
                    accessToken = tokenService.deEncryptToken(encryptedToken);
                } catch (Exception e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                    throw new RuntimeException("非法令牌");
                }
                /**
                 * 1、解析token，然后获取用户相关信息
                 * 2、到uc中获取用户相关信息，这里可以修改换成其他的
                 * 3、将获取到的基本信息存储到本地缓存中
                 */
                GlobalConst.Jwt.StoreStrategy storeStrategy = globalConfig.getJwt().getStoreStrategy();
                Claims claims = null;
                // 有状态jwt时、有效性判断
                if (storeStrategy == GlobalConst.Jwt.StoreStrategy.DIS || storeStrategy == GlobalConst.Jwt.StoreStrategy.PROCESS) {
                    claims = Jwts.parser().setSigningKey(globalConfig.getJwt().getSecret()).
                            setAllowedClockSkewSeconds(System.currentTimeMillis()).build().parseClaimsJws(accessToken).getBody();
                    // 判断jwt是否过期，不从jwt本身进行判断，可能给用户带来不好的体验
                    String tokenId = claims.get(GlobalConst.Jwt.ID, String.class);
                    if (StringUtils.isBlank(tokenId)) {
                        log.error("非法访问、无法从jwt中获取id值，生成JWT的服务端未设置该值");
                        throw new GlobalException(GlobalConst.Response.Msg.ILLEGAL_TOKEN, GlobalConst.Response.Code.SERVER_ERROR);
                    }
                    // 根据tokenId 检查对应的值是否过期，如果过期说明jwt也过期了，如果没有过期，那么就重置过期时间，达到jwt无感知续签
                    SysTokenCache userToken = starterCache.get(SysTokenCache.class, tokenId, SysTokenCache.class);
                    if (userToken == null) {
                        // 服务器没有该token，过期了
                        log.error("token 未获取到，可能原因：(1) 太长时间没有访问，过期了，属于正常情况 (2)token 缓存用的不是同一个中间件");
                        throw new GlobalException(GlobalConst.Response.Msg.EXPIRE_TOKEN, GlobalConst.Response.Code.NO_LOGIN);
                    }
                    if (!StringUtils.equals(userToken.getToken(), encryptedToken)) {
                        log.error("用户: {} 可能被人顶下线了", userToken.getRealName());
                        throw new GlobalException(GlobalConst.Response.Msg.OFF_LINE_TOKEN, GlobalConst.Response.Code.NO_LOGIN);
                    }
                    JwtClientBind clientBind = globalConfig.getJwt().getClientBind();
                    // 校验客户端是否正确、防止泄露后通过其他方式访问
                    if (clientBind.getEnable()) {
                        HttpServletRequest request = null;
                        try {
                            request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
                        } catch (Exception e) {
                            log.error("无法获取 request 对象，请确定是否通过http协议触发 ");
                        }
                        if (request != null) {
                            List<String> clientProperties = clientBind.getProperties();
                            for (String clientProperty : clientProperties) {
                                if ("userAgent".equals(clientProperty)) {
                                    String currentUserAgent = request.getHeader("User-Agent");
                                    if (userToken.getUserAgent() != null && !userToken.getUserAgent().equals(currentUserAgent)) {
                                        log.error("token疑似泄露，非法访问，颁发者User-Agent: {} , 当前访问者User-Agent: {}", userToken.getUserAgent(), currentUserAgent);
                                        throw new GlobalException(GlobalConst.Response.Msg.ILLEGAL_ACCESS, GlobalConst.Response.Code.NO_LOGIN);
                                    }
                                }
                            }
                        }
                    }
                    // 续签：更新过期时间
                    Date dead = new Date(System.currentTimeMillis() + globalConfig.getJwt().getExpiration() * 1000);
                    userToken.setDeadDate(dead);
                    starterCache.put(SysTokenCache.class, tokenId, userToken);
                } else {
                    try {
                        claims = Jwts.parser().setSigningKey(globalConfig.getJwt().getSecret()).build().parseClaimsJws(accessToken).getBody();
                    } catch (ExpiredJwtException e) {
                        log.error("当前jwt策略为none，token已到期，无法访问， {}", ExceptionUtils.getStackTrace(e));
                        throw new GlobalException(GlobalConst.Response.Msg.EXPIRE_TOKEN, GlobalConst.Response.Code.NO_LOGIN);
                    }
                }
                // 解析token，然后获取用户相关信息
                tmpUsername = claims.get(GlobalConst.Jwt.USER_NAME, String.class);
            }
        }
        final String username = tmpUsername;
        QianXingRestSso restSso = thirdPartyRestSso;
        String thirdPartyToken = (String) token.getPrincipal();
        CurrentUserBase thirdPartyUserBase = thirdPartyUser;
        // 到缓存中获取和业务相关的其他基本信息
        CurrentUserBase currentUser = UserUtils.getUser(username, (k) -> {
            CurrentUserBase currentUserResp = new CurrentUserBase();
            if (restSso == null || !restSso.getRemotePermissionVerify()) {
                log.debug("缓存中没有用户基本信息以及权限等信息，到数据库中获取");
                SysUserEntity dbUser = userService.getByUserName(username);
                if (dbUser == null) {
                    throw new GlobalException(String.format("用户 %s 不存在", username));
                }
                String moduleCode = globalConfig.getModule().getModuleCode();
                List<SysMenuEntity> navList = menuService.getNavMenuList(dbUser.getId(), moduleCode);
                List<SysRoleEntity> roleList = roleService.getRoleList(dbUser.getId());
                currentUserResp.setRoleListByEntity(roleList, moduleCode);
                currentUserResp.setId(dbUser.getId());
                currentUserResp.setName(dbUser.getUsername());
                currentUserResp.setOrgId(dbUser.getOrgId());
                currentUserResp.setRealName(dbUser.getRealName());
                currentUserResp.setTenantId(dbUser.getTenantId());
                currentUserResp.setPermissions(navList);
                currentUserResp.setDataRule(navList);
                log.debug("封装用户信息到缓存:{}", navList);
                return currentUserResp;
            }
            // 调用第三方系统获取权限进行校验
            log.debug("缓存中没有用户基本信息以及权限等信息，调用 {} 接口获取", restSso.getRemotePermissionUrl());
            Map<String, String> header = new HashMap<>();
            header.put(restSso.getTokenKey(), thirdPartyToken);
            long start = System.currentTimeMillis();
            try (Response response = HttpUtils.get(restSso.getRemotePermissionUrl(), header)) {
                if (!response.isSuccessful()) {
                    log.error("单点登录获取用户 {} 权限失败，url: {} 响应内容: {}", username, restSso.getRemotePermissionUrl(), response.body().string());
                    throw new GlobalException("单点登录校验失败");
                }
                String respBody = response.body().string();
                log.info("单点登录获取用户 {} 权限结束，url: {} 耗时: {} 毫秒", username, restSso.getRemotePermissionUrl(), System.currentTimeMillis() - start);
                JSONObject respObj = JSON.parseObject(respBody);
                int code = respObj.getInt("code");
                if (code != 200) {
                    log.error("单点登录获取用户 {} 权限失败，url: {} 响应内容: {}", username, restSso.getRemotePermissionUrl(), respBody);
                    throw new GlobalException("单点登录校验失败");
                }
                JSONArray permissionArr = respObj.getJSONArray("data");
                List<SysMenuEntity> menuList = new ArrayList<>();
                for (Object menuObj : permissionArr) {
                    JSONObject menuJsonObj = (JSONObject) menuObj;
                    if (!menuJsonObj.isNull("permissions")) {
                        SysMenuEntity menu = new SysMenuEntity();
                        menu.setPermissions(menuJsonObj.getString("permissions"));
                        menuList.add(menu);
                    }
                }
                thirdPartyUserBase.setPermissions(menuList);
            } catch (Exception e) {
                log.error("单点登录获取用户 {} 权限失败，url: {} ", username, restSso.getRemotePermissionUrl());
                log.error(ExceptionUtils.getStackTrace(e));
            }
            return thirdPartyUserBase;
        });
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(currentUser, token.getPrincipal(), getName());
        return info;
    }

    @SneakyThrows
    public static void main(String[] args) {
        URI uri = new URI("http://192.168.81.26:8080/job/data-room-ui%E8%87%AA%E5%8A%A8%E5%8F%91%E5%B8%83%E5%8C%85/");
        String protocol = uri.getScheme(); // http/https
        String host = uri.getHost(); // example.com 或 sub.example.com
        int port = uri.getPort();
        System.out.println(protocol + " = " + host + " = " + port);
    }
}
