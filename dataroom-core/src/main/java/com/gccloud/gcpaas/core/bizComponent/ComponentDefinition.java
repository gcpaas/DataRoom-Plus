package com.gccloud.gcpaas.core.bizComponent;

import lombok.Data;

@Data
public class ComponentDefinition {
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 组件描述
     */
    private String desc;
    /**
     * 组件名称
     */
    private String name;
    /**
     * 最终被哪个组件实现
     */
    private String implName;
    /**
     * 组件路径
     */
    private String path;
    /**
     * 组件缩略图
     */
    private String thumb;
    /**
     * 是否定义class
     */
    private Boolean classDef = false;
    /**
     * 是否注册panel
     */
    private Boolean panelInstall = false;
    /**
     * 是否注册组件
     */
    private Boolean componentInstall = false;
    /**
     * 类定义优先级
     */
    private Integer classDefPriority = 1;
}
