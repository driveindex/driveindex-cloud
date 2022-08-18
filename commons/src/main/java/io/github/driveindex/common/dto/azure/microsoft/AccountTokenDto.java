package io.github.driveindex.common.dto.azure.microsoft;

import com.fasterxml.jackson.annotation.JsonIgnore;
import feign.form.FormProperty;
import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/9 16:41
 */
public class AccountTokenDto {
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Response extends AccountDetailDto {
        private String tokenType;
        private Long expiresIn;
        private String scope;
        private String accessToken;
        private String refreshToken;

        @JsonIgnore
        public Long getExpiresTime() {
            return System.currentTimeMillis() + expiresIn * 1000;
        }
    }

    @Data
    public static class Request implements Serializable {
        @FormProperty("client_id")
        private String clientId;
//        @FormProperty("client_secret")
//        private String clientSecret;
        @FormProperty("refresh_token")
        private String refreshToken;
        @FormProperty("grant_type")
        private String grantType;
    }
}
