package com.gccloud.gcpaas.core.resources;

import com.gccloud.gcpaas.core.bean.Resp;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 素材库
 */
@Tag(name = "素材库")
@ApiSort(value = 20)
@Slf4j
@RequestMapping("/dataRoom/resource")
@RestController
public class ResourceController {
    
    @GetMapping({"/icon/list"})
    @ResponseBody
    public Resp<List<ResourceDefinition>> iconResourceList() throws IOException {
        log.info("获取系统内置图标资源");
        List<ResourceDefinition> resDefList = new ArrayList<>();
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/maxv/resources/icon/**");
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            ResourceDefinition resDef = new ResourceDefinition();
            resDef.setId(UUID.randomUUID().toString().replace("-", ""));
            resDef.setName("image-chart");
            resDef.setDesc(filename);
            resDef.setPath("/static/maxv/resources/icon");
            resDef.setThumb(filename);
            resDefList.add(resDef);
        }
        return Resp.success(resDefList);
    }


    @GetMapping({"/image/list"})
    @ResponseBody
    public Resp<List<ResourceDefinition>> imageResourceList() throws IOException {
        log.info("获取系统内置背景资源");
        List<ResourceDefinition> resDefList = new ArrayList<>();
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/maxv/resources/bg/**");
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            ResourceDefinition resDef = new ResourceDefinition();
            resDef.setId(UUID.randomUUID().toString().replace("-", ""));
            resDef.setName("image-chart");
            resDef.setDesc(filename);
            resDef.setPath("/static/maxv/resources/bg");
            resDef.setThumb(filename);
            resDefList.add(resDef);
        }
        return Resp.success(resDefList);
    }
}