package io.github.driveindex.admin;

import io.github.driveindex.common.util.GsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author sgpublic
 * @Date 2022/8/3 8:19
 */
@Import(GsonUtil.class)
@EnableFeignClients
@SpringBootApplication
public class AdminServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }
}
