package com.gccloud.gcpaas.core.dataset.service;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gccloud.gcpaas.core.DataRoomConstant;
import com.gccloud.gcpaas.core.bean.KeyVal;
import com.gccloud.gcpaas.core.dataset.DatasetRunRequest;
import com.gccloud.gcpaas.core.dataset.DatasetRunResponse;
import com.gccloud.gcpaas.core.dataset.bean.DatasetInputParam;
import com.gccloud.gcpaas.core.dataset.bean.DatasetOutputParam;
import com.gccloud.gcpaas.core.dataset.bean.HttpDataset;
import com.gccloud.gcpaas.core.entity.DatasetEntity;
import com.gccloud.gcpaas.core.util.ExecuteUtils;
import com.gccloud.gcpaas.core.util.ParamUtils;
import com.gccloud.gcpaas.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service(value = DataRoomConstant.Dataset.TYPE.HTTP + DataRoomConstant.Dataset.TYPE.SERVICE_NAME)
public class HttpDatasetService extends AbstractDatasetService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public DatasetRunResponse run(DatasetRunRequest datasetRunRequest, DatasetEntity datasetEntity) {
        DatasetRunResponse datasetRunResponse = new DatasetRunResponse();
        HttpDataset httpDataset = (HttpDataset) datasetEntity.getDataset();
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

            Map<String, String> headers = new HashMap<>();
            List<KeyVal> headerList = httpDataset.getHeaderList();
            for (KeyVal keyVal : headerList) {
                headers.put(keyVal.getKey(), keyVal.getVal());
            }
            String url = httpDataset.getUrl();
            String body = httpDataset.getBody();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                // 替换URL
                url = ParamUtils.replace(url, entry.getKey(), entry.getValue().toString());
                // 替换header
                Set<String> headerKeySet = headers.keySet();
                for (String headerKey : headerKeySet) {
                    String headerVal = headers.get(headerKey);
                    headerVal = ParamUtils.replace(headerVal, entry.getKey(), entry.getValue().toString());
                    headers.put(headerKey, headerVal);
                }
                // 替换请求体
                body = ParamUtils.replace(body, entry.getKey(), entry.getValue().toString());
            }
            Object data = null;
            if (DataRoomConstant.Dataset.HTTP_DATASET.METHOD.GET.equalsIgnoreCase(httpDataset.getMethod())) {
                String respBody = HttpUtil.createGet(url).addHeaders(headers).execute().body();
                data = OBJECT_MAPPER.readValue(respBody, Object.class);
            } else if (DataRoomConstant.Dataset.HTTP_DATASET.METHOD.POST.equalsIgnoreCase(httpDataset.getMethod())) {
                String respBody = HttpUtil.createPost(url).addHeaders(headers).body(body).execute().body();
                data = OBJECT_MAPPER.readValue(respBody, Object.class);
            }
            if (StringUtils.isNotBlank(httpDataset.getRespScript())) {
                params.put("respData", data);
                data = ExecuteUtils.run(httpDataset.getRespScript(), params);
            }
            List<DatasetOutputParam> outputParamList = new ArrayList<>();
            if (data instanceof List) {
                List list = (List) data;
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
            datasetRunResponse.setOutputParamList(outputParamList);
            datasetRunResponse.setData(data);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            datasetRunResponse.setData(new ArrayList<>());
        }
        return datasetRunResponse;
    }
}
