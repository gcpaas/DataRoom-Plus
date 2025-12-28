package com.gccloud.gapaas.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 页面定义
 */
@Data
public class BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 唯一编码
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String code;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 租户编码
     */
    private String tenantCode;
    /**
     * 逻辑删除
     */
    @TableLogic(delval = "1", value = "0")
    private String delFlag = "0";
}
