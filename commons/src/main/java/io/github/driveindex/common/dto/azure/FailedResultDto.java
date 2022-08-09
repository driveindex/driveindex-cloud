package io.github.driveindex.common.dto.azure;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/9 9:34
 */
@Data
public class FailedResultDto implements Serializable {
    private String error;
    private String errorDescription;
    private LinkedList<Integer> errorCodes;
    private String timestamp;
    private String traceId;
    private String correlationId;
}
