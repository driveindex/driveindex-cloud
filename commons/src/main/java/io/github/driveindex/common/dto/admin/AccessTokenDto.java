package io.github.driveindex.common.dto.admin;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/6 9:21
 */
@Data
public class AccessTokenDto implements Serializable {
    private String token;
}
