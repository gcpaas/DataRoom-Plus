package com.gccloud.gcpaas.core.shiro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * 当前登录的用户
 */
@Data
public class LoginUser {
    /**
     * 标识用户在服务器中的唯一身份
     */
    @JsonIgnore
    private String tokenId;

    /**
     * 用户的主键
     */
    private String id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户真实名称
     */
    private String realName;

    /**
     * 所属租户
     */
    private String tenantCode;

    /**
     * 角色编码集合
     */
    private List<String> roleCodeList;
}
