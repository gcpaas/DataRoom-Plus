package com.gccloud.gapaas.core.dataset.service;

import com.gccloud.gapaas.core.MaxvConstant;
import com.gccloud.gapaas.core.dataset.DatasetRunRequest;
import com.gccloud.gapaas.core.dataset.DatasetRunResponse;
import com.gccloud.gapaas.core.dataset.bean.DatasetInputParam;
import com.gccloud.gapaas.core.dataset.bean.DatasetOutputParam;
import com.gccloud.gapaas.core.dataset.bean.GroovyDataset;
import com.gccloud.gapaas.core.entity.DatasetEntity;
import com.gccloud.gapaas.core.util.ExecuteUtils;
import com.gccloud.gapaas.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service(value = MaxvConstant.Dataset.TYPE.GROOVY + MaxvConstant.Dataset.TYPE.SERVICE_NAME)
public class GroovyDatasetService extends AbstractDatasetService {

    @Override
    public DatasetRunResponse run(DatasetRunRequest datasetRunRequest, DatasetEntity datasetEntity) {
        DatasetRunResponse datasetRunResponse = new DatasetRunResponse();
        GroovyDataset groovyDataset = (GroovyDataset) datasetEntity.getDataset();
        try {
            Map<String, Object> params = new HashMap<>();
            List<DatasetInputParam> inputParamList = datasetEntity.getInputList();
            Map<String, DatasetInputParam> inputParamMap = new HashMap<>();
            for (DatasetInputParam inputParam : inputParamList) {
                inputParamMap.put(inputParam.getName(), inputParam);
                params.put(inputParam.getName(), TypeUtils.transType(inputParam.getDefaultVal(), inputParam.getType()));
            }
            Map<String, Object> realInputParam = datasetRunRequest.getInputParam();
            // 组合实际传入参数
            realInputParam.entrySet().forEach((entry) -> {
                DatasetInputParam defInputParam = inputParamMap.get(entry.getKey());
                if (defInputParam != null) {
                    params.put(entry.getKey(), TypeUtils.transType(entry.getValue(), defInputParam.getType()));
                } else {
                    params.put(entry.getKey(), entry.getValue());
                }
            });
            Object data = ExecuteUtils.run(groovyDataset.getScript(), params);
            List<DatasetOutputParam> outputParamList = new ArrayList<>();
            if (data instanceof List) {
                List list = (List) data;
                if (list.size() > 0) {
                    Object o = list.get(0);
                    if (o instanceof Map) {
                        Map map = (Map) o;
                        for (Object key : map.keySet()) {
                            DatasetOutputParam outputParam = new DatasetOutputParam();
                            outputParam.setName(key.toString());
                            outputParam.setDesc(key.toString());
                            Object val = map.get(key);
                            outputParam.setType(TypeUtils.parseType(val));
                            outputParamList.add(outputParam);
                        }
                    }
                }
            }
            datasetRunResponse.setOutputParamList(outputParamList);
            datasetRunResponse.setData(data);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            datasetRunResponse.setData(new ArrayList<>());
        }
        return datasetRunResponse;
    }
}
