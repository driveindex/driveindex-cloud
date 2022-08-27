package io.github.driveindex.admin;

import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.manager.ConfigManager;
import io.github.driveindex.common.util.GsonUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author sgpublic
 * @Date 2022/8/3 8:19
 */
@Import({GsonUtil.class, ConfigManager.class})
@EnableFeignClients
@SpringBootApplication
public class AdminServiceApplication {
    public static void main(String[] args) {
        DriveIndexCommon.Bootstrap(AdminServiceApplication.class)
                .setPort(11421)
                .setSqlSchema("bootstrap", "admin")
                .setDatasource("admin")
                .run(args);
    }
}
