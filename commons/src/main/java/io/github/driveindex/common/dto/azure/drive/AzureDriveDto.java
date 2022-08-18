package io.github.driveindex.common.dto.azure.drive;

import io.github.driveindex.common.dto.azure.microsoft.AzureTokenDto;
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
        private String prefix;
        private String suffix;
    }
}
