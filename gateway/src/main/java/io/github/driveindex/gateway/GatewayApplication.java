package io.github.driveindex.gateway;

import io.github.driveindex.common.DriveIndexCommon;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author sgpublic
 * @Date 2022/8/2 8:30
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        new DriveIndexCommon.Bootstrap(GatewayApplication.class)
                .setPort(11411)
                .run(args);
    }
}
