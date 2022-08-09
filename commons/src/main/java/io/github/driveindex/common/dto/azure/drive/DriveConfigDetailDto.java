package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:02
 */
@Data
public class DriveConfigDetailDto implements Serializable {
    private String calledName;
    private String dirHome;
}
