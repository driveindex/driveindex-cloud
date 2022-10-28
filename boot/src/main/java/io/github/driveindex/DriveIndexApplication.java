package io.github.driveindex;

import io.github.driveindex.common.DriveIndexCommon;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sgpublic
 * @Date 2022/8/27 12:09
 */
@SpringBootApplication
public class DriveIndexApplication {
    public static void main(String[] args) {
        DriveIndexCommon.Bootstrap(DriveIndexApplication.class)
                .setSqlSchema("bootstrap", "admin", "azure")
                .setPort(11411)
                .setDatasource(DriveIndexCommon.APPLICATION_BASE_NAME.toLowerCase())
                .addSpringDocScan("io.github.driveindex.controller")
                .addSpringDocScan("io.github.driveindex.admin.controller")
                .addSpringDocScan("io.github.driveindex.azure.controller")
                .addSpringDocScan("io.github.driveindex.common.controller")
                .run(args);
    }
}
