package com.gccloud.gapaas.core.dataset.bean;

import lombok.Data;

/**
 * 关系型数据库数据集，如：MySQL、Oracle、PG等
 */
@Data
public class RelationalDataset extends BaseDataset {
    /**
     * 执行的数据源编码
     */
    private String datasourceCode;
    /**
     * 请求脚本
     */
    private String reqScript;
    /**
     * 执行的SQL
     */
    private String sql;
    /**
     * 响应脚本
     */
    private String respScript;
}
