package com.gccloud.gapaas.core.resources;

import com.gccloud.gapaas.core.SuperController;
import com.gccloud.gapaas.core.bean.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 内置系统素材
 */
@Slf4j
@RequestMapping("/maxv/sysResource")
@Controller
public class ResourceController extends SuperController {

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