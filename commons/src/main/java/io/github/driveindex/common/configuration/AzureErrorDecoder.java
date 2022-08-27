package io.github.driveindex.common.configuration;

import feign.Response;
import feign.codec.DecodeException;
import feign.codec.ErrorDecoder;
import io.github.driveindex.common.dto.azure.microsoft.AzureFailedResultDto;
import io.github.driveindex.common.exception.AzureDecodeException;
import io.github.driveindex.common.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/14 19:18
 */
@Slf4j
@Component
public class AzureErrorDecoder implements ErrorDecoder {
    private final Default defaultDecoder = new Default();

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

            Object error = dto.get("error");
            if (error instanceof String) {
                AzureFailedResultDto resultDto = GsonUtil.fromJson(json.toString(),
                        AzureFailedResultDto.class);
                return new AzureDecodeException(response.status(), resultDto.getError(),
                        resultDto.getErrorDescription(), response.request());
            }
            //noinspection unchecked
            Map<String, Object> content = (Map<String, Object>) error;
            return new AzureDecodeException(response.status(), (String) content.get("code"),
                    (String) content.get("message"), response.request());
        } catch (RuntimeException e) {
            return defaultDecoder.decode(s, response);
        } catch (Exception e) {
            return new DecodeException(response.status(), e.getMessage(), response.request(), e);
        }
    }
}