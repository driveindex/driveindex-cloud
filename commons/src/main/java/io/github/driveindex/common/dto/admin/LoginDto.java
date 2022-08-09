package io.github.driveindex.common.dto.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/6 12:34
 */
@Data
public class LoginDto implements Serializable {
    private String password;
}
