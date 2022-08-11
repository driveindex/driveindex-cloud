package io.github.driveindex.admin.h2.dao;

import io.github.driveindex.common.dto.azure.drive.DriveConfigDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DriveConfigDao extends DriveConfigDetailDto {
    private String id;
    private Boolean enable;

    private Long defaultTargetFlag;
}
