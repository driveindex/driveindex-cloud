package io.github.driveindex.azure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/15 9:24
 */
@FeignClient("admin-service")
public interface ConfigClient {
    @GetMapping("/config")
    Map<String, Object> getConfig(
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String drive
    );
}
