package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.module.DriveConfigModule;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDetailDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:04
 */
@RequiredArgsConstructor
@RestController
public class DriveConfigController {
    private final DriveConfigModule configModule;


    @PostMapping("/api/admin/drive_config/{aClient}/{aAccount}/{aDrive}")
    public ResponseData saveDrive(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @PathVariable String aDrive,
            @RequestBody DriveConfigDetailDto dto
    ) {
        boolean save = configModule.save(aClient, aAccount, aDrive, dto);
        return save ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }


    @PostMapping("/api/admin/drive_config/enabled/{aClient}/{aAccount}/{aDrive}")
    public ResponseData enable(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @PathVariable String aDrive,
            Boolean enabled
    ) {
        boolean setEnable = configModule.enable(aClient, aAccount, aDrive, enabled);
        return setEnable ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @PostMapping("/api/admin/drive_config/delete/{aClient}/{aAccount}/{aConfig}")
    public ResponseData delete(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @PathVariable String aConfig
    ) {
        configModule.delete(aClient, aAccount, aConfig);
        return SuccessResult.SAMPLE;
    }

    @PostMapping("/api/admin/drive_config/default/{aClient}/{aAccount}/{aConfig}")
    public ResponseData setDefault(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @PathVariable String aConfig
    ) {
        boolean setDefault = configModule.setDefault(aClient, aAccount, aConfig);
        return setDefault ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }
}
