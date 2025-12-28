package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 登录的token、未来扩展
 */
@Data
@TableName(value = "dr_token")
public class TokenEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户名
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String username;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;
}
