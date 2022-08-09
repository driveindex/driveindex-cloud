package io.github.driveindex.azure.controller;

import io.github.driveindex.azure.module.DriveConfigModule;
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
@RestController("/api/admin/drive_config")
public class DriveConfigController {
    private final DriveConfigModule config;


    @PostMapping("/{aClient}/{aAccount}/{aDrive}")
    public ResponseData saveDrive(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @PathVariable String aDrive,
            @RequestBody DriveConfigDetailDto dto
    ) {
        boolean save = config.save(aClient, aAccount, aDrive, dto);
        return save ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }


    @PostMapping("/enabled/{aClient}/{aAccount}/{aDrive}")
    public ResponseData enable(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @PathVariable String aDrive,
            Boolean enabled
    ) {
        boolean setEnable = config.enable(aClient, aAccount, aDrive, enabled);
        return setEnable ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @PostMapping("/delete/{aClient}/{aAccount}")
    public ResponseData delete(@PathVariable String aClient, @PathVariable String aAccount) {
        config.delete(aClient, aAccount);
        return SuccessResult.SAMPLE;
    }
}
