package io.github.driveindex.common.dto.azure.common;

import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/9 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountTokenDto extends AccountDetailDto {
    private String tokenType;
    private Integer expiresIn;
    private String scope;
    private String accessToken;
    private String refreshToken;
    private String idToken;
}
