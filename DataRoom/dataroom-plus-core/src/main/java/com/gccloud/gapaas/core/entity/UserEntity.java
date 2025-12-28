package com.gccloud.gapaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 用户
 */
@Data
@TableName(value = "dr_user", autoResultMap = true)
public class UserEntity extends BaseEntity {
    /**
     * 账号
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态
     */
    private String state;
    /**
     * 角色编码
     */
    private List<String> roleCodeList;
}
