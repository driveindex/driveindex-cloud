package io.github.driveindex.controller;

import io.github.driveindex.admin.controller.AzureConfigController;
import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.admin.module.AzureAccountModule;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.admin.module.DriveConfigModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SpringBoot 不再需要远程调用接口获取信息，本地调用即可。
 * @author sgpublic
 * @Date 2022/8/27 14:14
 */
@Component
public class AzureConfigControllerImpl extends AzureConfigController {
    @Autowired
    public AzureConfigControllerImpl(AzureClientModule clientModule, AzureAccountModule accountModule, DriveConfigModule driveModule, AzureTokenClient tokenClient) {
        super(clientModule, accountModule, driveModule, tokenClient);
    }
}
