package io.github.driveindex.common.logback;

import io.github.driveindex.common.dto.result.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author sgpublic
 * @Date 2022/8/5 21:36
 */
@FeignClient(LoggingClient.SERVICE)
public interface LoggingClient {
    String SERVICE = "logging-service";

    @PostMapping("/log")
    ResponseData sendLoggingEvent(LoggingData event);
}