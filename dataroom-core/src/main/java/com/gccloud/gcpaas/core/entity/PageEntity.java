package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gccloud.gcpaas.core.constant.PageStatus;
import com.gccloud.gcpaas.core.constant.PageType;
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
    private PageType pageType = PageType.PAGE;
    /**
     * 所属目录编码
     */
    private String parentCode = "root";
    /**
     * 页面描述
     */
    private String remark;
    /**
     * 缩略图、封面
     */
    private String thumbnail;
    /**
     * 页面状态
     */
    private PageStatus pageStatus = PageStatus.DESIGN;
}
