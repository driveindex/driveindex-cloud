package io.github.driveindex.common.dto.azure.file;

import io.github.driveindex.common.dto.azure.drive.AzureDriveDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/16 18:11
 */
@Data
public abstract class AzureContentDto<T> implements Serializable {
    @Schema(description = "代号信息")
    private AzureDriveDto.CalledName calledName;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "类型")
    private String mineType;

    @Schema(description = "详情", oneOf = {
            DirContentDto.FileList.class, FileItemDto.class
    })
    private T content;
}
