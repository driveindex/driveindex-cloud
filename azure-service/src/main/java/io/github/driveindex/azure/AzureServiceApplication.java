package io.github.driveindex.azure;

import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.manager.ConfigManager;
import io.github.driveindex.common.util.GsonUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

/**
 * @author sgpublic
 * @Date 2022/8/3 9:05
 */
@Import({GsonUtil.class, ConfigManager.class})
@EnableFeignClients
@Configuration
@SpringBootApplication
public class AzureServiceApplication {
    public static void main(String[] args) {
        new DriveIndexCommon.Bootstrap(AzureServiceApplication.class)
                .setPort(11422)
                .setSqlSchema()
                .setDatasource("azure")
                .run(args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean("cacheCleanerScheduler")
    protected ThreadPoolTaskScheduler createScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("cache-cleaner-");
        taskScheduler.setAwaitTerminationSeconds(5);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
