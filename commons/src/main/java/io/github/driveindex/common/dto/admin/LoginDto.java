package io.github.driveindex.common.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/6 12:34
 */
@Data
public class LoginDto implements Serializable {
    @Schema(description = "管理员密码", required = true)
    private String password;
}
