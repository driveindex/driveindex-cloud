package io.github.driveindex.common.dto.azure.microsoft;

import feign.form.FormProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/9 9:13
 */
public class DeviceCodeDto {
    @Data
    public static class Response implements Serializable {
        private String userCode;
        private String expiresIn;
        private String interval;
        private String verificationUri;

        private String tag;
    }

    @Data
    public static class Request implements Serializable {
        @FormProperty("client_id")
        private String clientId;
        @FormProperty("scope")
        private String scope;
    }

    @Data
    public static class Check implements Serializable {
        @FormProperty("client_id")
        private String clientId;
        @FormProperty("device_code")
        private String deviceCode;
        @FormProperty("grant_type")
        private String grantType;
    }
}