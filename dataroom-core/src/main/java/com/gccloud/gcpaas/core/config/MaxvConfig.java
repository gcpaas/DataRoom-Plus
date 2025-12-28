package com.gccloud.gcpaas.core.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "maxv")
@Data
public class MaxvConfig {
    private WebConfigBean web = new WebConfigBean();
}
