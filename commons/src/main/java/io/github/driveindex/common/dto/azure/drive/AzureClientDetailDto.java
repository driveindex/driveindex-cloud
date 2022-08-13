package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:00
 */
@Data
public class AzureClientDetailDto implements Serializable, Cloneable {
    public static final String PLACEHOLDER_CLIENT_SECRET = "placeholder";

    private String calledName;
    private String clientId;
    private String clientSecret;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public AzureClientDetailDto clone() {
        AzureClientDetailDto newObj = new AzureClientDetailDto();
        newObj.setCalledName(this.getCalledName());
        newObj.setClientId(this.getClientId());
        newObj.setClientSecret(this.getClientSecret());
        return newObj;
    }
}
