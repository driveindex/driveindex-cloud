package io.github.driveindex.azure.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/15 11:21
 */
public interface AzureClient {
    String BASE_URL = "https://graph.microsoft.com";

    @GetMapping("/v1.0/me/drive/root{path}/children")
    Map<String, Object> listFile(
            @PathVariable String path,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/v1.0/me/drive/root")
    Map<String, Object> getRoot(@RequestHeader("Authorization") String token);
}
