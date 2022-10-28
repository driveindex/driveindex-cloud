package io.github.driveindex.common.dto.azure.drive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/14 15:43
 */
@Data
public class DeviceCodeCheckDto implements Serializable {
    @Schema(description = "从 /api/admin/azure_account/device_code/{aClient}/{aAccount} 接口获取到的 tag")
    private String tag;
}
