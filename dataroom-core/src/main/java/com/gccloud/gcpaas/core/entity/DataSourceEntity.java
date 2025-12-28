package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gccloud.gcpaas.core.datasource.bean.BaseDataSource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 数据源定义
 */
@Data
@Schema
@TableName(value = "dr_data_source", autoResultMap = true)
public class DataSourceEntity extends BaseEntity {
    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 数据源类型
     */
    @Schema(description = "数据源类型")
    @NotBlank(message = "数据源类型不能为空")
    private String dataSourceType;
    /**
     * 实际数据源
     */
    @Schema(description = "数据源配置")
    @NotNull(message = "数据源不能为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private BaseDataSource dataSource;
}
