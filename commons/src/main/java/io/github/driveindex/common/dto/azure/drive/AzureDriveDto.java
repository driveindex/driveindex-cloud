package io.github.driveindex.common.dto.azure.drive;

import io.github.driveindex.common.dto.azure.microsoft.AzureTokenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/15 11:29
 */
@Data
public class AzureDriveDto implements Serializable {

    private AzureTokenDto token;
    private String dirHome;
    private final CalledName calledName = new CalledName();

    @Data
    public static class CalledName implements Serializable {
        @Schema(description = "代号前缀")
        private String prefix;
        @Schema(description = "代号后缀")
        private String suffix;
    }
}
