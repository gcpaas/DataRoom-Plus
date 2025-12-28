package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 页面、包含仪表盘、大屏
 */
@Data
@TableName("dr_page")
public class PageEntity extends BaseEntity {
    /**
     * 页面名称
     */
    private String name;
    /**
     * 页面编码
     */
    private String code;
    /**
     * 页面类型
     */
    private String pageType;
    /**
     * 所属目录编码
     */
    private String parentCode;
    /**
     * 页面描述
     */
    private String remark;
    /**
     * 缩略图、封面
     */
    private String thumbnail;
    /**
     * 页面配置
     */
    private String pageConfig;
    /**
     * 页面状态
     */
    private String state;
}
