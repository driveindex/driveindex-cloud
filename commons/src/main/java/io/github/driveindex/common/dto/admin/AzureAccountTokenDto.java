package io.github.driveindex.common.dto.admin;

import io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AzureAccountTokenDto extends AccountTokenDto.Response {
    @Schema(description = "账号配置 ID")
    private String id;
    @Schema(description = "当前账号配置所属 Client ID")
    private String parentClient;
}
