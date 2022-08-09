package io.github.driveindex.azure.h2.dao;

import io.github.driveindex.common.dto.azure.common.AccountTokenDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountTokenDao extends AccountTokenDto {
    private String id;
    private String parentClientId;
    private Boolean enable;

    private Long defaultTargetFlag;
}
