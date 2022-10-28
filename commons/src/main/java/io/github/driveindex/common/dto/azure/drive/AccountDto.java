package io.github.driveindex.common.dto.azure.drive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/9 11:50
 */

@Data
public
class AccountDto implements Serializable {
    @Schema(description = "账号配置 ID")
    private String id;
    @Schema(description = "账号配置信息详情")
    private AccountDetailDto detail;
    @Schema(description = "是否默认")
    private Boolean isDefault = false;

    @Schema(description = "当前账号下属目录配置")
    private List<DriveConfigDto> child;
}