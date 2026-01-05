package com.gccloud.gcpaas.core.page.bean;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class PageConfig extends BasePageConfig {
    /**
     * 页面基础配置
     */
    private JSONObject pageConfig = new JSONObject();
    /**
     * 全局变量
     */
    private JSONArray globalVariableList = new JSONArray();
    /**
     * 图表列表
     */
    private JSONArray chartList = new JSONArray();

    @Override
    public void init() {

    }
}
