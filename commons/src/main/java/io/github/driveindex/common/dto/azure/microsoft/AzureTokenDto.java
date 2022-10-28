package io.github.driveindex.common.dto.azure.microsoft;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/15 8:26
 */
@Data
public class AzureTokenDto implements Serializable {
    private String accessToken;
    private String tokenType;

    @Override
    public String toString() {
        return tokenType + " " + accessToken;
    }
}
