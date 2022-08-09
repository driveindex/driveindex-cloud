package io.github.driveindex.logging;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author sgpublic
 * @Date 2022/8/5 20:38
 */
@EnableDiscoveryClient
@MapperScan("io.github.driveindex.logging.h2.repository")
@SpringBootApplication
public class LoggingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoggingServiceApplication.class, args);
    }
}
