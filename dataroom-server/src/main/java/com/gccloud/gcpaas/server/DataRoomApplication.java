package com.gccloud.gcpaas.server;

import com.gccloud.gcpaas.core.bean.Rsa;
import com.gccloud.gcpaas.core.user.service.TokenService;
import com.gccloud.gcpaas.core.util.RsaUtils;
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
import java.util.Base64;

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
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        Rsa rsa = RsaUtils.generateRsaKeyPair();
        log.info("------------以下信息可用于配置文件配置，每次重启随机生成-----------\n\n");
        log.info("JWT密钥: {}", base64Key);
        log.info("公钥: {}", rsa.getPublicKey());
        log.info("私钥: {}", rsa.getPrivateKey());
        log.info("\n\b-----------------------------------------------------------");
        SpringApplication.run(DataRoomApplication.class, args);
    }
}
