package io.github.driveindex.common.dto.azure.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sgpublic
 * @Date 2022/8/9 9:13
 */
@Data
public class DeviceCodeDto implements Serializable {
    private String userCode;
    private String expiresIn;
    private String interval;

    private String extraInfo;
}