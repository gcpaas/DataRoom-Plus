package com.gccloud.gcpaas.core.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.constant.DataRoomRole;
import com.gccloud.gcpaas.core.entity.UserEntity;
import com.gccloud.gcpaas.core.mapper.UserMapper;
import com.gccloud.gcpaas.core.user.service.UserService;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 */
@Tag(name = "用户管理")
@ApiSort(value = 200)
@RestController
@Controller
@RequestMapping("/dataRoom/user")
public class UserController {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;


    @GetMapping("/list")
    @RequiresRoles(value = DataRoomRole.MANAGER)
    @Operation(summary = "列表查询", description = "根据名称查询")
    @Parameters({@Parameter(name = "username", description = "用户名称", in = ParameterIn.QUERY)})
    public Resp<List<UserEntity>> list(@RequestParam(name = "username", required = false) String username) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserEntity::getId, UserEntity::getUsername, UserEntity::getRealName, UserEntity::getRoleCodeList, UserEntity::getState);
        queryWrapper.orderByDesc(UserEntity::getUpdateDate);
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like(UserEntity::getUsername, username);
        }
        List<UserEntity> list = userMapper.selectList(queryWrapper);
        return Resp.success(list);
    }

    @GetMapping("/detail/{username}")
    @RequiresRoles(value = DataRoomRole.MANAGER)
    @Operation(summary = "详情", description = "根据用户名查询")
    @Parameters({@Parameter(name = "username", description = "用户名", in = ParameterIn.PATH)})
    public Resp<UserEntity> detail(@PathVariable("username") String username) {
        UserEntity user = userService.getByUsername(username);
        return Resp.success(user);
    }

    @PostMapping("/insert")
    @RequiresRoles(value = DataRoomRole.MANAGER)
    @Operation(summary = "新增", description = "新增用户")
    public Resp<String> insert(@RequestBody UserEntity user) {
        userMapper.insert(user);
        return Resp.success(user.getId());
    }

    @PostMapping("/update")
    @RequiresRoles(value = DataRoomRole.MANAGER)
    @Operation(summary = "更新", description = "更新用户")
    public Resp<String> update(@RequestBody UserEntity user) {
        userMapper.updateById(user);
        return Resp.success(user.getId());
    }

    @PostMapping("/delete/{username}")
    @RequiresRoles(value = DataRoomRole.MANAGER)
    @Operation(summary = "删除", description = "根据用户名称删除")
    @Parameters({@Parameter(name = "username", description = "用户名", in = ParameterIn.PATH)})
    public Resp<Void> delete(@PathVariable("username") String username) {
        userService.deleteByUsername(username);
        return Resp.success(null);
    }
}