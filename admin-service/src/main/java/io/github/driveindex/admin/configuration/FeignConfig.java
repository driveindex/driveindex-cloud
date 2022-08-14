package io.github.driveindex.admin.configuration;

import feign.Contract;
import feign.Feign;
import feign.Request;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.common.dto.azure.AzureFailedResultDto;
import io.github.driveindex.common.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * @author sgpublic
 * @Date 2022/8/14 12:59
 */
@Import(FeignClientsConfiguration.class)
@Configuration
@RequiredArgsConstructor
public class FeignConfig {
    private final Decoder decoder;
    private final Encoder encoder;
    private final Contract contract;

    private final AzureErrorDecoder errorDecoder;

    @Bean
    public AzureTokenClient createAzureTokenClient() {
        return Feign.builder().encoder(encoder).decoder(decoder)
                .contract(contract).errorDecoder(errorDecoder)
                .target(AzureTokenClient.class, AzureTokenClient.BASE_URL);
    }

    @Slf4j
    @Component
    public static class AzureErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String s, Response response) {
            try (Reader input = response.body().asReader(StandardCharsets.UTF_8)) {
                StringBuilder json = new StringBuilder();
                char[] buffer = new char[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    json.append(buffer, 0, length);
                }
                AzureFailedResultDto dto = GsonUtil.fromJson(json.toString(),
                        AzureFailedResultDto.class);
                return new AzureDecodeException(response.status(), dto.getError(),
                        dto.getErrorDescription(), response.request());
            } catch (Exception e) {
                return new DecodeException(response.status(),
                        e.getMessage(), response.request(), e);
            }
        }
    }

    public static class AzureDecodeException extends DecodeException {
        private final String code;

        public AzureDecodeException(int status, String code, String message, Request request) {
            super(status, message, request);
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
