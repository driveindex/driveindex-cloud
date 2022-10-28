package io.github.driveindex.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author sgpublic
 * @Date 2022/8/13 10:38
 */
@Data
public class PasswordDto {
    @Schema(description = "旧密码", required = true)
    private String oldPass;
    @Schema(description = "新密码", required = true)
    private String newPass;
    @Schema(description = "新密码确认", required = true)
    private String repeatPass;
}
