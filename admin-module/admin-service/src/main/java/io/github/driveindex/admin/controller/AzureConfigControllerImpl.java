package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.admin.module.AzureAccountModule;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.admin.module.DriveConfigModule;
import io.github.driveindex.common.dto.azure.drive.AzureDriveDto;
import io.github.driveindex.common.dto.result.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sgpublic
 * @Date 2022/8/27 14:13
 */

@RestController
public class AzureConfigControllerImpl extends AzureConfigController {
    @Autowired
    public AzureConfigControllerImpl(AzureClientModule clientModule, AzureAccountModule accountModule, DriveConfigModule driveModule, AzureTokenClient tokenClient) {
        super(clientModule, accountModule, driveModule, tokenClient);
    }

    @Schema(implementation = AzureDriveDto.class)
    @Operation(summary = "查询目录配置", description = "获取目录配置详情")
    @GetMapping("/config")
    @Override
    public ResponseData getConfig(
            @Schema(description = "Client 配置 ID")
            @RequestParam(required = false) String client,
            @Schema(description = "账号配置 ID")
            @RequestParam(required = false) String account,
            @Schema(description = "目录配置 ID")
            @RequestParam(required = false) String drive
    ) {
        return super.getConfig(client, account, drive);
    }
}
