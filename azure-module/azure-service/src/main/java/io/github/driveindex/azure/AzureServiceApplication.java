package io.github.driveindex.azure;

import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.configuration.MyBatisPagerConfig;
import io.github.driveindex.common.manager.ConfigManager;
import io.github.driveindex.common.util.GsonUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sgpublic
 * @Date 2022/8/3 9:05
 */
@Import({GsonUtil.class, ConfigManager.class, MyBatisPagerConfig.class})
@EnableFeignClients
@Configuration
@SpringBootApplication
public class AzureServiceApplication {
    public static void main(String[] args) {
        DriveIndexCommon.Bootstrap(AzureServiceApplication.class)
                .setPort(11422)
                .setSqlSchema("bootstrap", "azure")
                .setDatasource("azure")
                .run(args);
    }
}
