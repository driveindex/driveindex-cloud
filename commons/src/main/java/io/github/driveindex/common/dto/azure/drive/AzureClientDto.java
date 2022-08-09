package io.github.driveindex.common.dto.azure.drive;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/9 11:50
 */
@Data
public class AzureClientDto implements Serializable {
    private String id;
    private AzureClientDetailDto detail;

    private List<AccountDto> child;
}