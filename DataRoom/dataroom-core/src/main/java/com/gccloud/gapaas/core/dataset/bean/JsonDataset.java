package com.gccloud.gapaas.core.dataset.bean;

import lombok.Data;

@Data
public class JsonDataset extends BaseDataset {
    /**
     * json文本
     */
    private String json;
}
