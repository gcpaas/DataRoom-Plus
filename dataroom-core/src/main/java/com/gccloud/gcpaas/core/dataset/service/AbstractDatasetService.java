package com.gccloud.gcpaas.core.dataset.service;


import com.gccloud.gcpaas.core.dataset.DatasetRunRequest;
import com.gccloud.gcpaas.core.dataset.DatasetRunResponse;
import com.gccloud.gcpaas.core.entity.DatasetEntity;

public abstract class AbstractDatasetService {

    /**
     * 数据集运行
     *
     * @param datasetRunRequest
     * @param datasetEntity
     * @return
     */
    public abstract DatasetRunResponse run(DatasetRunRequest datasetRunRequest, DatasetEntity datasetEntity);
}