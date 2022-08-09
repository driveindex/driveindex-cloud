package io.github.driveindex.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.driveindex.common.dto.result.FailedResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author sgpublic
 * @Date 2022/8/4 10:12
 */
@Order(-1)
@Slf4j
@RequiredArgsConstructor
@Configuration
public class GlobalHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @NonNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(throwable);
        }
        log.warn("handle exception", throwable);

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (throwable instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) throwable).getStatus());
        }

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                if (throwable instanceof NotFoundException) {
                    return bufferFactory.wrap(objectMapper.writeValueAsBytes(FailedResult.SERVICE_UNAVAILABLE));
                }
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(FailedResult.INTERNAL_SERVER_ERROR));
            } catch (JsonProcessingException e) {
                log.error("Error writing response", throwable);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
