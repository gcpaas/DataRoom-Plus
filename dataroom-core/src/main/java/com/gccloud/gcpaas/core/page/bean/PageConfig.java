package com.gccloud.gcpaas.core.page.bean;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class PageConfig extends BasePageConfig {
    /**
     * 页面基础配置
     */
    private JSONObject pageConfig;
    /**
     * 全局变量
     */
    private JSONArray globalVariableList;
    /**
     * 图表列表
     */
    private JSONArray chartList;

    @Override
    public void init() {

    }
}
