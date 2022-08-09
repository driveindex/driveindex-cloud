package io.github.driveindex.azure;

import io.github.driveindex.common.util.GsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author sgpublic
 * @Date 2022/8/3 9:05
 */
@Import(GsonUtil.class)
@SpringBootApplication
public class AzureServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AzureServiceApplication.class, args);
    }
}
