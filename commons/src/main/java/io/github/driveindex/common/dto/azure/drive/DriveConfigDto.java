package io.github.driveindex.common.dto.azure.drive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/9 11:53
 */
@Data
public class DriveConfigDto implements Serializable {
    @Schema(description = "账号配置 ID")
    private String id;
    @Schema(description = "账号配置信息详情")
    private DriveConfigDetailDto detail;
    @Schema(description = "是否默认")
    private Boolean isDefault = false;
}
