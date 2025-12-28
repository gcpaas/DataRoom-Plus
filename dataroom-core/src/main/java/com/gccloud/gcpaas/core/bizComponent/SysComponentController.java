package com.gccloud.gcpaas.core.bizComponent;

import com.gccloud.gcpaas.core.SuperController;
import com.gccloud.gcpaas.core.bean.Resp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内置系统组件
 */
@RequestMapping("/maxv/sysComponent")
@Controller
public class SysComponentController extends SuperController {

    private static final Logger log = LoggerFactory.getLogger(SysComponentController.class);

    @Resource
    private SysComponentService componentCacheService;

    @GetMapping({"/text/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> textChart() {
        log.info("获取系统内置文本图表组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/text");
        return Resp.success(list);
    }

    @GetMapping({"/table/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> listChart() {
        log.info("获取系统内置表格图表组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/table");
        return Resp.success(list);
    }

    @GetMapping({"/shape/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> shapeChart() {
        log.info("获取系统内置形状图表组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/shape");
        return Resp.success(list);
    }

    @GetMapping({"/icon/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> iconChart() {
        log.info("获取系统内置Icon图表组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/icon");
        return Resp.success(list);
    }

    @GetMapping({"/chart/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> echartsChart() {
        log.info("获取系统内置Echarts图表组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/echarts");
        return Resp.success(list);
    }

    @GetMapping({"/map/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> mapChart() {
        log.info("获取系统内置地图图表组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/map");
        return Resp.success(list);
    }

    @GetMapping({"/media/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> mediaChart() {
        log.info("获取系统内置多媒体图表组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/media");
        return Resp.success(list);
    }

    @GetMapping({"/decoration/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> decorationChart() {
        log.info("获取系统内置图表装饰组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/decoration");
        return Resp.success(list);
    }

    @GetMapping({"/border/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> borderChart() {
        log.info("获取系统内置图表边框组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/border");
        return Resp.success(list);
    }

    @GetMapping({"/iframe/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> iframeChart() {
        log.info("获取系统内置iframe组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/iframeChart");
        return Resp.success(list);
    }

    @GetMapping({"/container/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> containerChart() {
        log.info("获取系统内置容器组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/container");
        return Resp.success(list);
    }

    @GetMapping({"/other/list"})
    @ResponseBody
    public Resp<List<ComponentDefinition>> otherChart() {
        log.info("获取系统内置其他组件");
        List<ComponentDefinition> list = componentCacheService.getComponentDefinitionByParentPath("/static/maxv/components/button");
        return Resp.success(list);
    }
}