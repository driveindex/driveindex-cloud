package io.github.driveindex.azure.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author sgpublic
 * @Date 2023/2/7 10:07
 */
@Data
@EqualsAndHashCode(exclude = {"deltaUrl"})
@AllArgsConstructor
@NoArgsConstructor
@TableName("azure_delta")
public class AzureDeltaEntity {
    private String clientId;
    private String accountId;
    private String deltaUrl;
}
