package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gccloud.gcpaas.core.datasource.bean.BaseDataSource;
import lombok.Data;

/**
 * 数据源定义
 */
@Data
@TableName(value = "dr_data_source", autoResultMap = true)
public class DataSourceEntity extends BaseEntity {
    /**
     * 名称
     */
    private String name;
    /**
     * 数据源类型
     */
    private String dataSourceType;
    /**
     * 实际数据源
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private BaseDataSource dataSource;
}
