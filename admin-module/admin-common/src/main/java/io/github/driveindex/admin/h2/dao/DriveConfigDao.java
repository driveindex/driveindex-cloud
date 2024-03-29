package io.github.driveindex.admin.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drive_config")
public class DriveConfigDao extends DriveConfigDetailDto {
    public static final String DEFAULT_ID = "default";

    private String id;

    private String parentClient;
    private String parentAccount;
}
