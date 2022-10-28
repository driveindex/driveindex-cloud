package io.github.driveindex.admin.h2.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author sgpublic
 * @Date 2022/8/7 18:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("account_token")
public class AccountTokenDao extends AccountTokenDto.Response {
    @Schema(description = "账号配置 ID")
    private String id;
    @Schema(description = "当前账号配置所属 Client ID")
    private String parentClient;

    @Schema(description = "用于判断当前配置是否为默认配置的标记，最大时为默认值，前端列表可按此字段降序排序。")
    private Long defaultTargetFlag = -(new Date().getTime());
}
