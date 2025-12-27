package com.gccloud.gapaas.core;

import com.gccloud.gapaas.core.bean.PageVo;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public abstract class SuperController {
    protected String getResourceAsString(String resourceName) throws IOException {
        InputStream stream = SuperController.class.getClassLoader().getResourceAsStream(resourceName);
        String content = IOUtils.toString(stream, "utf-8");
        return content;
    }

    public PageVo toPageView(com.baomidou.mybatisplus.extension.plugins.pagination.Page page) {
        PageVo<Object> pageView = new PageVo<>();
        pageView.setCurrent(page.getCurrent());
        pageView.setSize(page.getSize());
        pageView.setTotal(page.getTotal());
        pageView.setData(page.getRecords());
        return pageView;
    }

    public com.baomidou.mybatisplus.extension.plugins.pagination.Page toPage(PageVo pageView) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Object> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        page.setSize(pageView.getSize());
        page.setCurrent(pageView.getCurrent());
        return page;
    }
}
