package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.module.DriveConfigModule;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDetailDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:04
 */
@Tag(name = "目录配置")
@RequiredArgsConstructor
@RestController
public class DriveConfigController {
    private final DriveConfigModule configModule;


    @Operation(summary = "创建目录配置", description = "新建一个目录配置")
    @PostMapping("/api/admin/drive_config/{aClient}/{aAccount}/{aDrive}")
    public ResponseData saveDrive(
            @Schema(description = "Client 配置 ID")
            @PathVariable String aClient,
            @Schema(description = "账号配置 ID")
            @PathVariable String aAccount,
            @Schema(description = "目录配置 ID")
            @PathVariable String aDrive,
            @RequestBody DriveConfigDetailDto dto
    ) {
        boolean save = configModule.save(aClient, aAccount, aDrive, dto);
        return save ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @Operation(summary = "删除目录配置", description = "删除一个目录配置")
    @PostMapping("/api/admin/drive_config/delete/{aClient}/{aAccount}/{aDrive}")
    public ResponseData delete(
            @Schema(description = "Client 配置 ID")
            @PathVariable String aClient,
            @Schema(description = "账号配置 ID")
            @PathVariable String aAccount,
            @Schema(description = "目录配置 ID")
            @PathVariable String aDrive
    ) {
        configModule.delete(aClient, aAccount, aDrive);
        return SuccessResult.SAMPLE;
    }
}
