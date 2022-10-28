package io.github.driveindex.common.dto.result;

import io.github.driveindex.common.util.GsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author sgpublic
 * @Date 2022/8/3 13:04
 */
@Schema(description = "基础响应内容")
@RequiredArgsConstructor
public abstract class ResponseData implements Serializable {
    @Schema(description = "响应代码", example = "200")
    public final Integer code;
    @Schema(description = "响应信息", example = "success.")
    public final String message;

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }

    public byte[] toBytes() {
        return toString().getBytes(StandardCharsets.UTF_8);
    }
}
