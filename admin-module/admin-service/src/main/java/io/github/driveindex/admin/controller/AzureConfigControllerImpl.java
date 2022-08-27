package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.admin.module.AzureAccountModule;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.admin.module.DriveConfigModule;
import io.github.driveindex.common.dto.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/config")
    @Override
    public ResponseData getConfig(String client, String account, String drive) {
        return super.getConfig(client, account, drive);
    }
}
