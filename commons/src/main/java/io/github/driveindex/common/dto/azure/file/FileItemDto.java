package io.github.driveindex.common.dto.azure.file;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/3 12:05
 */
@Data
public class FileItemDto implements Serializable {
    private String name;
    private String mineType;
    private AzureItemDto info;
    private AzureItemDto.Detail detail;
}
