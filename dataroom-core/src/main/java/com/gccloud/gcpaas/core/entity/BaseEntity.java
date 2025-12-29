package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "主键")
    private String id;
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createDate;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createUser;
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateDate;
    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateUser;
    /**
     * 租户编码
     */
    @Schema(description = "租户编码")
    private String tenantCode;
    /**
     * 逻辑删除
     */
    @Schema(description = "删除标识(0：正常，1：删除)",hidden = true)
    @TableLogic(delval = "1", value = "0")
    private String delFlag = "0";
}
