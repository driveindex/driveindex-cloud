package io.github.driveindex.common.dto.azure.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/3 12:05
 */
@Data
public class FileItemDto implements Serializable {
    @Schema(description = "名称")
    private String name;
    @Schema(description = "类型")
    private String mineType;
    @Schema(description = "文件信息")
    private AzureItemDto info;
    @Schema(description = "文件详情", oneOf = {
            DirDetailDto.class, FileDetailDto.class
    })
    private AzureItemDto.Detail detail;
}
