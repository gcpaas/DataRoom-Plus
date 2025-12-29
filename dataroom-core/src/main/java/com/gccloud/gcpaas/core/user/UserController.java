package com.gccloud.gcpaas.core.user;

import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.constant.DataRoomRole;
import com.gccloud.gcpaas.core.shiro.LoginUser;
import com.gccloud.gcpaas.core.user.service.UserService;
import com.gccloud.gcpaas.core.util.LoginUserUtils;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private UserService userService;

    @GetMapping("/current")
    @RequiresRoles(value = DataRoomRole.SHARER)
    @Operation(summary = "登录用户", description = "获取当前登录用户信息")
    public Resp<LoginUser> current() {
        LoginUser currentUser = LoginUserUtils.getCurrentUser();
        return Resp.success(currentUser);
    }
}