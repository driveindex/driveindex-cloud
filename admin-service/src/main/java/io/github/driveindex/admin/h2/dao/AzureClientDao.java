package io.github.driveindex.admin.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.driveindex.common.dto.azure.drive.AzureClientDetailDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("azure_client")
public class AzureClientDao extends AzureClientDetailDto {
    private String id;

    private Long defaultTargetFlag = -(new Date().getTime());
}
