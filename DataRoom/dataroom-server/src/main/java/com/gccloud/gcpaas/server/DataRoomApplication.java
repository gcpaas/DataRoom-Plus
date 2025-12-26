package com.gccloud.gcpaas.server;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DataRoomApplication {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.execute("select 1");
    }

    public static void main(String[] args) {
        SpringApplication.run(DataRoomApplication.class, args);
    }
}
