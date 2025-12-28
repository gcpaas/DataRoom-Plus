package com.gccloud.gapaas.core.bizComponent;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class SysComponentService {
    /**
     * 返回给页面使用的json字符串
     */
    private static String COMPONENT_DEF_STR;
    /**
     * 缓存起来的内置组件定义
     */
    private static List<ComponentDefinition> COMPONENT_DEF_CACHE = new ArrayList<>();
    /**
     * 提取vue文件中组件名称
     */
    private static final Pattern VUE_COMPONENT_NAME_PATTERN = Pattern.compile("\\sname:\\s*['\"](.*)['\"]");

    private static final String SPLIT = File.separator;

    static {
        try {
            PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = patternResolver.getResources(ResourceUtils.CLASSPATH_URL_PREFIX + SPLIT + "static" + SPLIT + "maxv" + SPLIT + "components" + SPLIT + "**");
            Map<String, ComponentDefinition> componentDefinitionMap = new HashMap<>();
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename.indexOf(".") < 0) {
                    continue;
                }
                String description = resource.getDescription();
                String path = description.substring(description.indexOf("[") + 1, description.indexOf("]"));
                String parentPath = "";
                if (resource instanceof FileSystemResource) {
                    path = path.substring(path.indexOf(SPLIT + "static" + SPLIT + "maxv" + SPLIT + "components"));
                    parentPath = path.substring(0, path.lastIndexOf(SPLIT));
                } else {
                    path = SPLIT + path;
                    parentPath = path.substring(0, path.lastIndexOf(SPLIT));
                }
                ComponentDefinition compDef = componentDefinitionMap.computeIfAbsent(parentPath, (key) -> {
                    ComponentDefinition componentDefinition = new ComponentDefinition();
                    componentDefinition.setId(UUID.randomUUID().toString().replace("-", ""));
                    componentDefinition.setPath(key);
                    COMPONENT_DEF_CACHE.add(componentDefinition);
                    return componentDefinition;
                });
                if (filename.equals("panel.vue")) {
                    compDef.setPanelInstall(true);
                } else if (filename.equals("index.vue")) {
                    String content = IOUtils.toString(resource.getInputStream(), "utf-8");
                    Matcher matcher = VUE_COMPONENT_NAME_PATTERN.matcher(content);
                    if (matcher.find()) {
                        String name = matcher.group();
                        name = name.split(":")[1].trim();
                        name = name.substring(1, name.length() - 1);
                        compDef.setName(name);
                    }
                    compDef.setComponentInstall(true);
                    log.info("{} 提取到组件名称为 {}", resource.getDescription(), compDef.getName());
                } else if (filename.endsWith(".png") || filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".svg")) {
                    compDef.setThumb(filename);
                    String desc = FilenameUtils.getBaseName(filename);
                    compDef.setDesc(desc);
                } else if (filename.endsWith(".impl")) {
                    String implName = FilenameUtils.getBaseName(filename);
                    compDef.setImplName(implName);
                } else if (filename.equals("index.js")) {
                    compDef.setClassDef(true);
                }
            }
            Map<String, ComponentDefinition> componentDefinitionNameMap = new HashMap<>();
            for (ComponentDefinition compDef : componentDefinitionMap.values()) {
                if (StringUtils.isBlank(compDef.getName())) {
                    log.error("组件: {}  没有按照规范创建index.vue 和 name", compDef.getPath());
                    continue;
                }
                componentDefinitionNameMap.put(compDef.getName(), compDef);
            }
            for (ComponentDefinition componentDefinition : componentDefinitionNameMap.values()) {
                if (StringUtils.isBlank(componentDefinition.getThumb())) {
                    log.error("组件: {} 没有上传缩略图", componentDefinition.getPath());
                }
                if (StringUtils.isNotBlank(componentDefinition.getImplName()) && !StringUtils.equalsIgnoreCase(componentDefinition.getName(), componentDefinition.getImplName())) {
                    ComponentDefinition compDefImpl = componentDefinitionNameMap.get(componentDefinition.getImplName());
                    if (compDefImpl == null) {
                        log.error("组件 {} 依赖的组件 {} 未找到", componentDefinition.getPath(), componentDefinition.getImplName());
                        continue;
                    }
                    componentDefinition.setClassDefPriority(compDefImpl.getClassDefPriority() + 1);
                }
            }
            // 加载顺序从小到大排列
            COMPONENT_DEF_CACHE.sort(Comparator.comparing(ComponentDefinition::getClassDefPriority));
            ObjectMapper objectMapper = new ObjectMapper();
            COMPONENT_DEF_STR = objectMapper.writeValueAsString(COMPONENT_DEF_CACHE);
        } catch (Exception e) {
            log.error("读取组件注册列表失败", e);
        }
    }

    public List<String> getChartClassDefList() {
        List<String> list = new ArrayList<>();
        for (ComponentDefinition compDef : COMPONENT_DEF_CACHE) {
            if (compDef.getClassDef()) {
                list.add(compDef.getPath() + "/index.js");
            }
        }
        return list;
    }

    public String getComponentDefStr() {
        return COMPONENT_DEF_STR;
    }

    public List<ComponentDefinition> getComponentDefinitionByParentPath(String path) {
        List<ComponentDefinition> list = new ArrayList<>();
        for (ComponentDefinition compDef : COMPONENT_DEF_CACHE) {
            if (compDef.getPath().startsWith(path)) {
                list.add(compDef);
            }
        }
        return list;
    }

    public List<ComponentDefinition> getComponentDefinitionByParentPath(List<String> paths) {
        List<ComponentDefinition> list = new ArrayList<>();
        for (String path : paths) {
            for (ComponentDefinition compDef : COMPONENT_DEF_CACHE) {
                if (compDef.getPath().startsWith(path)) {
                    list.add(compDef);
                }
            }
        }
        return list;
    }

    public ComponentDefinition getComponentDefinitionByName(String name) {
        for (ComponentDefinition compDef : COMPONENT_DEF_CACHE) {
            if (StringUtils.isBlank(compDef.getName())) {
                continue;
            }
            if (compDef.getName().equals(name)) {
                return compDef;
            }
        }
        return null;
    }
}
