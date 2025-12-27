package com.gccloud.gapaas.core;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@MapperScan("com.xiaoka.maxv.**.mapper")
public class MaxvApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaxvApplication.class, args);
    }

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor paginationInterceptor = new MybatisPlusInterceptor();
        DbType dbType = DbType.getDbType("mysql");
        return paginationInterceptor;
    }
}