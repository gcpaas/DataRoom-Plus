package com.gccloud.gapaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 租户
 */
@Data
@TableName(value = "dr_tenant", autoResultMap = true)
public class TenantEntity extends BaseEntity {
    /**
     * 名称
     */
    private String name;
    /**
     * 租户编码
     */
    private String code;
    /**
     * 备注
     */
    private String remark;
}
