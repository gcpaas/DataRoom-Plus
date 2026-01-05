package com.gccloud.gcpaas.core.page.bean;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PageConfig extends BasePageConfig {

    @Override
    public void init() {

    }

    @Override
    public void compat() {
        JSONObject basicConfig = getBasicConfig();
        if (basicConfig == null || basicConfig.isEmpty()) {
            // 初始化基础配置
            String basicConfigJson = """
                    {
                        "background": {
                            "fill": 'color',
                            "color": '#fff',
                            "url": '',
                            "repeat": 'no-repeat'
                         }
                    }
                    """;
            basicConfig = JSON.parseObject(basicConfigJson);
            setBasicConfig(basicConfig);
        }
    }
}
