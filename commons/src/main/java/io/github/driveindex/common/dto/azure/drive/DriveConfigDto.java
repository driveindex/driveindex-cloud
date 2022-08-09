package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/9 11:53
 */
@Data
public class DriveConfigDto implements Serializable {
    private String id;

    private DriveConfigDetailDto detail;
}
