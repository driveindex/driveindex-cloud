package io.github.driveindex.admin.feign;

import io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto;
import io.github.driveindex.common.dto.azure.microsoft.DeviceCodeDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/8 15:46
 */
public interface AzureTokenClient {
    String BASE_URL = "https://login.microsoftonline.com";

    String GRANT_TYPE_AUTHOR = "urn:ietf:params:oauth:grant-type:device_code";
    String GRANT_TYPE_REFRESH = "refresh_token";

    String SCOPE = "offline_access files.read.all";

    @PostMapping(value = "/common/oauth2/v2.0/devicecode", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getDeviceCode(DeviceCodeDto.Request dto);

    @PostMapping(value = "/common/oauth2/v2.0/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getToken(DeviceCodeDto.Check dto);

    @PostMapping(value = "/common/oauth2/v2.0/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> refreshToken(AccountTokenDto.Request dto);
}
