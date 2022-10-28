package io.github.driveindex.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:21
 */
@Data
public class AccessTokenDto implements Serializable {
    @Schema(description = "用户登陆凭证，使用请附加到 header 中，key 为 driveindex-authentication", example = "a5c2bca1aaz3...")
    private String token;
}
