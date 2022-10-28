package io.github.driveindex.common.dto.azure.drive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:00
 */
@Data
public class AzureClientDetailDto implements Serializable, Cloneable {
    public static final String PLACEHOLDER_CLIENT_SECRET = "placeholder";

    @Schema(description = "Client 配置代号")
    private String calledName;
    @Schema(description = "微软 Azure 服务提供的 ClientID")
    private String clientId;
//    private String clientSecret;
    @Schema(description = "是否启用")
    private Boolean enable = true;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public AzureClientDetailDto clone() {
        AzureClientDetailDto newObj = new AzureClientDetailDto();
        newObj.setCalledName(this.getCalledName());
        newObj.setClientId(this.getClientId());
//        newObj.setClientSecret(this.getClientSecret());
        newObj.setEnable(this.getEnable());
        return newObj;
    }
}
