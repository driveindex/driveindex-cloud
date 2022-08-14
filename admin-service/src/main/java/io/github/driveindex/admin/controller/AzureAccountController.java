package io.github.driveindex.admin.controller;

import feign.codec.DecodeException;
import io.github.driveindex.admin.configuration.FeignConfig;
import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.admin.h2.dao.AccountTokenDao;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.module.AzureAccountModule;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
import io.github.driveindex.common.dto.azure.drive.DeviceCodeCheckDto;
import io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto;
import io.github.driveindex.common.dto.azure.microsoft.DeviceCodeDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.github.driveindex.common.util.GsonUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/8 13:34
 */
@RequiredArgsConstructor
@RestController
public class AzureAccountController {
    private final AzureClientModule clientModule;
    private final AzureAccountModule accountModule;
    private final AzureTokenClient feign;

    @PostMapping("/api/admin/azure_account/{aClient}/{aAccount}")
    public ResponseData saveAccount(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @RequestBody AccountDetailDto calledName
    ) {
        boolean save = accountModule.save(aClient, aAccount, calledName);
        return save ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @Data
    private static class ExtraInfo implements Serializable {
        private String deviceCode;
    }

    @GetMapping("/api/admin/azure_account/device_code/{aClient}/{aAccount}")
    public ResponseData onDeviceCode(
            @PathVariable String aClient,
            @PathVariable String aAccount
    ) {
        AccountTokenDao account = accountModule.getAccount(aClient, aAccount);
        if (account == null) return FailedResult.NOT_FOUND;
        AzureClientDao parentClient = clientModule.getById(aClient);
        if (parentClient == null) return FailedResult.NOT_FOUND;

        DeviceCodeDto.Request dto = new DeviceCodeDto.Request();
        dto.setScope(AzureTokenClient.SCOPE);
        dto.setClientId(parentClient.getClientId());
        try {
            Map<String, Object> code = feign.getDeviceCode(dto);
            String json = GsonUtil.fromMap(code);
            DeviceCodeDto.Response deviceCodeDto = GsonUtil.fromJson(json, DeviceCodeDto.Response.class);
            ExtraInfo extraInfo = GsonUtil.fromJson(json, ExtraInfo.class);
            String encode = Base64.getEncoder().encodeToString(GsonUtil.toJson(extraInfo)
                    .getBytes(StandardCharsets.UTF_8));
            deviceCodeDto.setTag(encode);
            return SuccessResult.of(deviceCodeDto);
        } catch (DecodeException e) {
            return new FailedResult(-3002, "Device Code 申请出错：" + e.getMessage());
        }
    }

    private static final FailedResult LOGIN_PENDING = new FailedResult(-3001, "用户暂未完成登录");

    @PostMapping("/api/admin/azure_account/check_code/{aClient}/{aAccount}")
    public ResponseData onCheckDeviceCode(
            @PathVariable String aClient,
            @PathVariable String aAccount,
            @RequestBody DeviceCodeCheckDto dto
    ) {
        AccountTokenDao account = accountModule.getAccount(aClient, aAccount);
        if (account == null) return FailedResult.NOT_FOUND;
        AzureClientDao parentClient = clientModule.getById(account.getParentClient());
        if (parentClient == null) return FailedResult.NOT_FOUND;

        byte[] decode = Base64.getDecoder().decode(dto.getTag().getBytes(StandardCharsets.UTF_8));
        ExtraInfo info = GsonUtil.fromJson(new String(decode, StandardCharsets.UTF_8), ExtraInfo.class);
        DeviceCodeDto.Check check = new DeviceCodeDto.Check();
        check.setClientId(parentClient.getClientId());
        check.setDeviceCode(info.deviceCode);
        check.setGrantType(AzureTokenClient.GRANT_TYPE_AUTHOR);
        try {
            Map<String, Object> code = feign.getToken(check);
            String json = GsonUtil.fromMap(code);
            boolean saved = accountModule.saveToken(aClient, aAccount,
                    GsonUtil.fromJson(json, AccountTokenDto.Response.class));
            return saved ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
        } catch (FeignConfig.AzureDecodeException e) {
            if ("authorization_pending".equals(e.getCode())) return LOGIN_PENDING;
            return new FailedResult(-3003, "Device Code 检查出错：" + e.getMessage());
        }
    }


    @PostMapping("/api/admin/azure_account/enabled/{aClient}/{aAccount}")
    public ResponseData enable(@PathVariable String aClient, @PathVariable String aAccount, Boolean enabled) {
        boolean setEnable = accountModule.enable(aClient, aAccount, enabled);
        return setEnable ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @PostMapping("/api/admin/azure_account/delete/{aClient}/{aAccount}")
    public ResponseData delete(@PathVariable String aClient, @PathVariable String aAccount) {
        accountModule.delete(aClient, aAccount);
        return SuccessResult.SAMPLE;
    }

    @PostMapping("/api/admin/azure_account/default/{aClient}/{aAccount}")
    public ResponseData setDefault(@PathVariable String aClient, @PathVariable String aAccount) {
        boolean setDefault = accountModule.setDefault(aClient, aAccount);
        return setDefault ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }
}
