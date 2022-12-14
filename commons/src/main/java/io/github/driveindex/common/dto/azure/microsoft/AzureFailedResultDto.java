package io.github.driveindex.common.dto.azure.microsoft;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/9 9:34
 */
@Data
public class AzureFailedResultDto implements Serializable {
    private String error;
    private String errorDescription;
    private List<Integer> errorCodes;
    private String timestamp;
    private String traceId;
    private String correlationId;
}
