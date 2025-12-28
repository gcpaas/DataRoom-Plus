package com.gccloud.gcpaas.core.page.bean;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gccloud.gcpaas.core.constant.PageType;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "pageType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectoryPageConfig.class, name = PageType.DIRECTORY_TYPE),
        @JsonSubTypes.Type(value = VisualScreenPageConfig.class, name = PageType.VISUAL_SCREEN_TYPE),
        @JsonSubTypes.Type(value = PageConfig.class, name = PageType.PAGE_TYPE)
})
public class BasePageConfig {
}
