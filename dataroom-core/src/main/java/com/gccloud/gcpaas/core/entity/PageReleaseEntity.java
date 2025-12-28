package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * 发布态页面
 */
@Data
@TableName("`mv_page_release`")
public class PageReleaseEntity extends BaseEntity {
    /**
     * 页面编码
     */
    private String code;
    /**
     * 发布说明
     */
    private String remark;
    /**
     * 配置信息
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private PageEntity entityConfig;
}
