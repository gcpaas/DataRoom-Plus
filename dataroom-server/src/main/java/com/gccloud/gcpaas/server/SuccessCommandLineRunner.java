package com.gccloud.gcpaas.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;

@Slf4j
@Order(1)
@Component
public class SuccessCommandLineRunner implements CommandLineRunner {

    @jakarta.annotation.Resource
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        String LINE = "------------------------------------";
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("server.servlet.context-path", "");
        log.info(LINE);
        log.info("本地服务地址: http://localhost:{}{}", port, path);
        log.info("本地服务地址: http://127.0.0.1:{}{}", port, path);
        log.info("生产服务地址: http://{}:{}{}", ip, port, path);
        log.info("接口文档地址: http://{}:{}{}/doc.html", ip, port, path);
        // 获取 JVM 启动时间（毫秒）
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        long startTimeMillis = runtimeMxBean.getStartTime();
        log.info("启动耗时: {} 毫秒", System.currentTimeMillis() - startTimeMillis);
        log.info(LINE);
    }
}
