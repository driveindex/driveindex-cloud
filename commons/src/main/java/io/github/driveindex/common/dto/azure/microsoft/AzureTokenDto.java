package io.github.driveindex.common.dto.azure.microsoft;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/15 8:26
 */
public class AzureTokenDto implements Serializable {
    @Setter
    @Getter
    private String accessToken;

    @Setter
    @Getter
    private String tokenType;

    @Override
    public String toString() {
        return tokenType + " " + accessToken;
    }
}
