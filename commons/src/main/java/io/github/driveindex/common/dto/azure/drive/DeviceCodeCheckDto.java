package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/14 15:43
 */
@Data
public class DeviceCodeCheckDto implements Serializable {
    private String tag;
}
