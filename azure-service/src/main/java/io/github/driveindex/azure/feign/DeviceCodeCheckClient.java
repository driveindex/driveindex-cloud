package io.github.driveindex.azure.feign;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:46
 */
public interface DeviceCodeCheckClient {
    String GRANT_TYPE_AUTHOR = "urn:ietf:params:oauth:grant-type:device_code";

    @PostMapping("/")
    Map<String, Object> getTokenByDeviceCode(
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "device_code") String deviceCode,
            @RequestParam(name = "grant_type", defaultValue = GRANT_TYPE_AUTHOR) String grantType
    );
}
