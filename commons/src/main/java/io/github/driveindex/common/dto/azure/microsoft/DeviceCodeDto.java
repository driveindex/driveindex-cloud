package io.github.driveindex.common.dto.azure.microsoft;

import feign.form.FormProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/9 9:13
 */
public class DeviceCodeDto {
    @Data
    public static class Response implements Serializable {
        @Schema(description = "提供给用户的 DeviceCode")
        private String userCode;
        @Schema(description = "有效期，单位：秒")
        private String expiresIn;
        @Schema(description = "检查 DeviceCode 轮询时间间隔，单位：秒")
        private String interval;
        @Schema(description = "用于检查 DeviceCode 的 url")
        private String verificationUri;

        @Schema(description = "用于检查 DeviceCode 的 tag")
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