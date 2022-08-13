package io.github.driveindex.azure;

import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.util.GsonUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author sgpublic
 * @Date 2022/8/3 9:05
 */
@Import(GsonUtil.class)
@EnableFeignClients
@SpringBootApplication
public class AzureServiceApplication {
    public static void main(String[] args) {
        new DriveIndexCommon.Bootstrap(AzureServiceApplication.class)
                .setPort(11421)
                .setSqlSchema()
                .setDatasource("azure")
                .run(args);
    }
}
