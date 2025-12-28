package com.gccloud.gcpaas.core.shiro;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * oauth2过滤器
 */
@Slf4j
public class ShiroAuthFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        /**
         * 检查是否是数字签名
         */
        HttpServletRequest req = (HttpServletRequest) request;
        String token = TokenUtils.getToken((HttpServletRequest) request, globalConfig.getJwt());
        if (StringUtils.isBlank(token)) {
            return new ShiroAuthToken("");
        }
        return new ShiroAuthToken(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String sign = req.getHeader(SignUtils.U_SIGN);
        if (StringUtils.equals(SignUtils.DEFAULT_SIGN, sign)) {
            return executeLogin(request, response);
        }
        // 获取请求token，如果token不存在，直接返回401
        GlobalConfig globalConfig = SpringContextUtils.getBean(GlobalConfig.class);
        String token = TokenUtils.getToken((HttpServletRequest) request, globalConfig.getJwt());
        if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", getOrigin());
            httpResponse.setContentType("application/json;charset=utf-8");
            String json = new Gson().toJson(R.error(GlobalConst.Response.Code.SERVER_ERROR, GlobalConst.Response.Msg.NO_TOKEN));
            httpResponse.getWriter().print(json);
            return false;
        }
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", getOrigin());
        try {
            // 处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            R r = R.error(GlobalConst.Response.Code.NO_LOGIN, throwable.getMessage());
            String json = new Gson().toJson(r);
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {
            log.error(ExceptionUtils.getStackTrace(e1));
        }
        return false;
    }

    public HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public String getOrigin() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader("Origin");
    }
}
