package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:00
 */
@Data
public class AzureClientDetailDto implements Serializable {
    private String calledName;
    private String clientId;
    private String clientSecret;

    private Long defaultTargetFlag;
}
