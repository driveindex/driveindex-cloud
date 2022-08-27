package io.github.driveindex.azure.configuration;

import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.github.driveindex.azure.feign.AzureClient;
import io.github.driveindex.common.configuration.AzureErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author sgpublic
 * @Date 2022/8/14 19:18
 */
@Import({FeignClientsConfiguration.class, AzureErrorDecoder.class})
@Configuration
public class AzureFeignClientConfig {
    private final Feign.Builder builder;

    @Autowired
    public AzureFeignClientConfig(
            Encoder encoder, Decoder decoder,
            Contract contract, AzureErrorDecoder errorDecoder
    ) {
        this.builder = Feign.builder().encoder(encoder).decoder(decoder)
                .contract(contract).errorDecoder(errorDecoder);
    }

    @Bean
    public AzureClient createAzureClient() {
        return builder.target(AzureClient.class, AzureClient.BASE_URL);
    }
}
