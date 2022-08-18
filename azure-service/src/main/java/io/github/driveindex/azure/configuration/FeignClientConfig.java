package io.github.driveindex.azure.configuration;

import feign.Contract;
import feign.Feign;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import io.github.driveindex.azure.feign.AzureClient;
import io.github.driveindex.common.exception.AzureDecodeException;
import io.github.driveindex.common.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/14 19:18
 */
@Import(FeignClientsConfiguration.class)
@Configuration
public class FeignClientConfig {
    private final Feign.Builder builder;

    @Autowired
    public FeignClientConfig(
            Encoder encoder, Decoder decoder,
            Contract contract, AzureErrorDecoder errorDecoder
    ) {
        this.builder = Feign.builder().encoder(encoder).decoder(decoder)
                .contract(contract).errorDecoder(errorDecoder);
    }

    @Bean
    public AzureClient createAzureTokenClient() {
        return builder.target(AzureClient.class, AzureClient.BASE_URL);
    }

    @Slf4j
    @Component
    public static class AzureErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder.Default defaultDecoder = new Default();

        @Override
        public Exception decode(String s, Response response) {

            try (Reader input = response.body().asReader(StandardCharsets.UTF_8)) {
                StringBuilder json = new StringBuilder();
                char[] buffer = new char[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    json.append(buffer, 0, length);
                }
                Map<String, Object> dto = GsonUtil.toMap(json.toString());
                //noinspection unchecked
                Map<String, Object> error = (Map<String, Object>) dto.get("error");
                return new AzureDecodeException(response.status(), (String) error.get("code"),
                        (String) error.get("message"), response.request());
            } catch (RuntimeException e) {
                return defaultDecoder.decode(s, response);
            } catch (Exception e) {
                return new DecodeException(response.status(), e.getMessage(), response.request(), e);
            }
        }
    }
}
