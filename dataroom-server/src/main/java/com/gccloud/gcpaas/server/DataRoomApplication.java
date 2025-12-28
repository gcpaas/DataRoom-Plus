package com.gccloud.gcpaas.server;

import com.gccloud.gcpaas.core.user.service.TokenService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.crypto.SecretKey;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.gccloud.gcpaas")
@MapperScan("com.gccloud.gcpaas.**")
public class DataRoomApplication {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private TokenService tokenService;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("select 1");
        String token = tokenService.createToken("admin");
        log.info("模拟生成token: {}", token);
    }

    public static void main(String[] args) {
        // 生成一个安全的 256 位 HMAC 密钥
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // 将密钥转换为 Base64 编码的字符串z
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        log.info("生成的JWT密钥: {}", base64Key);
        SpringApplication.run(DataRoomApplication.class, args);
    }
}
