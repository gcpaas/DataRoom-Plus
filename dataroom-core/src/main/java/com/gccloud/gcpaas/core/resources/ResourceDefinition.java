package com.gccloud.gcpaas.core.resources;

import lombok.Data;

@Data
public class ResourceDefinition {
    /**
     * 唯一ID
     */
    private String id;
    /**
     * 资源名称
     */
    private String desc;
    /**
     * 图表类型
     */
    private String name;
    /**
     * 资源路径
     */
    private String path;
    /**
     * 缩略图
     */
    private String thumb;
}
