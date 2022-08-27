package io.github.driveindex.azure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

/**
 * @author sgpublic
 * @Date 2022/8/27 14:16
 */
@Configuration
public class CacheCleanerConfig {
    @Bean("cacheCleanerScheduler")
    protected ThreadPoolTaskScheduler createScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("cache-cleaner-");
        taskScheduler.setAwaitTerminationSeconds(5);
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
