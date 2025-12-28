package com.gccloud.gapaas.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dataroom")
public class DataRoomConfig {
    private String apiUrl;
}
