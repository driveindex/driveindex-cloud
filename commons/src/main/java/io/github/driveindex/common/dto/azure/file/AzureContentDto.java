package io.github.driveindex.common.dto.azure.file;

import io.github.driveindex.common.dto.azure.drive.AzureDriveDto;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/16 18:11
 */
@Data
public abstract class AzureContentDto<T> implements Serializable {
    private AzureDriveDto.CalledName calledName;
    private String name;
    private String mineType;

    private T content;
}
