package io.github.driveindex.azure.feign;

import kotlin.text.Charsets;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/8 15:46
 */
@FeignClient(url = AzureTokenClient.BASE_URL)
public interface AzureTokenClient {
    String BASE_URL = "https://login.microsoftonline.com";
    String GRANT_TYPE_REFRESH = "refresh_token";

    String SCOPE = URLEncoder.encode("offline_access files.read.all", Charsets.UTF_8);

    @PostMapping("/common/oauth2/v2.0/devicecode")
    Map<String, Object> getDeviceCode(
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "scope") String scope
    );

    @PostMapping("/common/oauth2/v2.0/token")
    Map<String, Object> refreshToken(
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "client_secret") String clientSecret,
            @RequestParam(name = "refresh_token") String refreshToken,
            @RequestParam(name = "grant_type", defaultValue = GRANT_TYPE_REFRESH) String grantType
    );
}
