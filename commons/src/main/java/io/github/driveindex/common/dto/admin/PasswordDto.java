package io.github.driveindex.common.dto.admin;

import lombok.Data;

/**
 * @author sgpublic
 * @Date 2022/8/13 10:38
 */
@Data
public class PasswordDto {
    private String oldPass;
    private String newPass;
    private String repeatPass;
}
