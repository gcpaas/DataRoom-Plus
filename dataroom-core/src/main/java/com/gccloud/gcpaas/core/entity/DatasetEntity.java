package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.gccloud.gcpaas.core.dataset.bean.BaseDataset;
import com.gccloud.gcpaas.core.dataset.bean.DatasetInputParam;
import com.gccloud.gcpaas.core.dataset.bean.DatasetOutputParam;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据集定义
 */
@Data
@TableName(value = "dr_dataset", autoResultMap = true)
public class DatasetEntity extends BaseEntity {
    /**
     * 数据源编码
     */
    private String dataSourceCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 数据集类型
     */
    private String datasetType;
    /**
     * 所属目录编码
     */
    private String parentCode;
    /**
     * 实际数据集
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private BaseDataset dataset;
    /**
     * 入参列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<DatasetInputParam> inputList = new ArrayList<>();
    /**
     * 出参列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<DatasetOutputParam> outputList = new ArrayList<>();
}
