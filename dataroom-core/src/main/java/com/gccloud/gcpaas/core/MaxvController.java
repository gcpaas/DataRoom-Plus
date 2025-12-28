package com.gccloud.gcpaas.core;

import com.gccloud.gcpaas.core.bizComponent.SysComponentService;
import com.gccloud.gcpaas.core.config.MaxvConfig;
import com.gccloud.gcpaas.core.config.WebConfigBean;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/maxv")
@Controller
public class MaxvController {

    @Resource
    private SysComponentService componentCacheService;
    @Resource
    private MaxvConfig maxvConfig;

    @GetMapping({"", "/index", "/home"})
    public String home(ModelMap modelMap) {
        WebConfigBean webConfig = maxvConfig.getWeb();
        modelMap.addAttribute("web.title", webConfig.getTitle());
        modelMap.addAttribute("web.footer", webConfig.getFooter());
        return "maxv/index";
    }


    @GetMapping({"/create"})
    public String create(ModelMap modelMap) {
        return "maxv/create";
    }

    @GetMapping({"/app"})
    public String pageList(ModelMap modelMap) {
        return "maxv/app/index";
    }

    @GetMapping({"/page/designer"})
    public String designer(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("chartClassDefList", componentCacheService.getChartClassDefList());
        return "maxv/page/pageDesigner";
    }

    @GetMapping({"page/preview"})
    public String preview(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("previewMode", "preview");
        return "maxv/page/pagePreview";
    }

    @GetMapping({"page/devPreview"})
    public String devPreview(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("previewMode", "devPreview");
        return "maxv/page/pagePreview";
    }

    @GetMapping({"/dashBoard/designer"})
    public String dashBoard(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("chartClassDefList", componentCacheService.getChartClassDefList());
        return "maxv/dashBoard/dashBoardDesigner";
    }

    @GetMapping({"/dashBoard/preview"})
    public String dashBoardPreview(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("chartClassDefList", componentCacheService.getChartClassDefList());
        modelMap.addAttribute("previewMode", "preview");
        return "maxv/dashBoard/dashBoardPreview";
    }

    @GetMapping({"/dashBoard/devPreview"})
    public String devDashBoardPreview(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("chartClassDefList", componentCacheService.getChartClassDefList());
        modelMap.addAttribute("previewMode", "devPreview");
        return "maxv/dashBoard/dashBoardPreview";
    }

    @GetMapping({"/h5DashBoard/designer"})
    public String dashBoardH5(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("chartClassDefList", componentCacheService.getChartClassDefList());
        return "maxv/h5DashBoard/h5DashBoardDesigner";
    }

    @GetMapping({"/h5DashBoard/preview"})
    public String h5DashBoardPreview(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("chartClassDefList", componentCacheService.getChartClassDefList());
        modelMap.addAttribute("previewMode", "preview");
        return "maxv/h5DashBoard/h5DashBoardPreview";
    }

    @GetMapping({"/h5DashBoard/devPreview"})
    public String devH5DashBoardPreview(ModelMap modelMap) {
        modelMap.addAttribute("COMPONENT_DEF_STR", componentCacheService.getComponentDefStr());
        modelMap.addAttribute("chartClassDefList", componentCacheService.getChartClassDefList());
        modelMap.addAttribute("previewMode", "devPreview");
        return "maxv/h5DashBoard/h5DashBoardPreview";
    }

    @GetMapping({"/dataCube"})
    public String dataCube(ModelMap modelMap) {
        return "maxv/dataCube/dataCube";
    }

    @GetMapping({"/datasource"})
    public String datasource(ModelMap modelMap) {
        return "maxv/datasource/index";
    }

    @GetMapping({"/dataset"})
    public String dataset(ModelMap modelMap) {
        return "maxv/dataset/index";
    }

    @GetMapping({"/bizChart/designer"})
    public String bizChartDesigner(ModelMap modelMap) {
        return "maxv/bizChart/designer";
    }

}