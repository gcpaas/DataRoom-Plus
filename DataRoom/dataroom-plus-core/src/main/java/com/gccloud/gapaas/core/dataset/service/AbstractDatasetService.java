package com.gccloud.gapaas.core.dataset.service;


import com.gccloud.gapaas.core.dataset.DatasetRunRequest;
import com.gccloud.gapaas.core.dataset.DatasetRunResponse;
import com.gccloud.gapaas.core.entity.DatasetEntity;

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