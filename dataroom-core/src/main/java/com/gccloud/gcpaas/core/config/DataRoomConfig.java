package com.gccloud.gcpaas.core.config;

import com.gccloud.gcpaas.core.config.bean.Jwt;
import com.gccloud.gcpaas.core.config.bean.Sso;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "dataroom")
public class DataRoomConfig {
    /**
     * jwt
     */
    private Jwt jwt = new Jwt();
    /**
     * 单点登录配置
     */
    private List<Sso> ssoList = new ArrayList<>();
}
