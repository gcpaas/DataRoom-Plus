package com.gccloud.gapaas.core.dataset.bean;

import com.gccloud.gapaas.core.bean.KeyVal;
import lombok.Data;

import java.util.List;

@Data
public class HttpDataset extends BaseDataset {
    private String url;
    private String method;
    private List<KeyVal> headerList;
    private String body;
    private String reqScript;
    private String respScript;
}
