package io.github.driveindex.common.exception;

import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author sgpublic
 * @Date 2022/8/16 20:33
 */
@Slf4j
@RestControllerAdvice
public class AzureExceptionHandler {
    @ExceptionHandler(AzureDecodeException.class)
    public ResponseData handleAzureDecodeException(
            AzureDecodeException exception,
            ServerHttpResponse response
    ) {
        log.warn("未捕获的 Azure 接口解析错误", exception);
        response.setStatusCode(HttpStatus.valueOf(exception.status()));
        return new FailedResult(-10001, exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseData handleRuntimeException(RuntimeException exception) {
        log.warn("未捕获的 RuntimeException", exception);
        return new FailedResult(-10002, exception.getMessage());
    }
}
