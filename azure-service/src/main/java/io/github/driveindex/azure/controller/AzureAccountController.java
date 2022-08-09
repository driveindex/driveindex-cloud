package io.github.driveindex.azure.controller;

import feign.Feign;
import io.github.driveindex.azure.feign.AzureTokenClient;
import io.github.driveindex.azure.feign.DeviceCodeCheckClient;
import io.github.driveindex.azure.h2.dao.AccountTokenDao;
import io.github.driveindex.azure.module.AzureAccountModule;
import io.github.driveindex.common.dto.azure.common.AccountTokenDto;
import io.github.driveindex.common.dto.azure.common.DeviceCodeDto;
import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
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
@RestController("/api/admin/azure_account")
public class AzureAccountController {
    private final AzureAccountModule accountModule;
    private final AzureTokenClient feign;

    @PostMapping("/{aClient}/{aAccount}")
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
        private String verificationUri;
    }

    @PostMapping("/device_code/{aClient}/{aAccount}")
    public ResponseData onDeviceCode(@PathVariable String aClient, @PathVariable String aAccount) {
        AccountTokenDao account = accountModule.getAccount(aClient, aAccount);
        if (account == null) return FailedResult.NOT_FOUND;

        Map<String, Object> code = feign.getDeviceCode(account.getParentClientId(),
                AzureTokenClient.SCOPE);
        if (code.containsKey("error")) {
            return new FailedResult(-3001, "Device Code 申请出错：" +
                    code.get("error_description"));
        }
        String json = GsonUtil.toJson(code);
        DeviceCodeDto deviceCodeDto = GsonUtil.fromJson(json, DeviceCodeDto.class);
        ExtraInfo extraInfo = GsonUtil.fromJson(json, ExtraInfo.class);
        byte[] encode = Base64.getEncoder().encode(GsonUtil.toJson(extraInfo)
                .getBytes(StandardCharsets.UTF_8));
        deviceCodeDto.setExtraInfo(new String(encode, StandardCharsets.UTF_8));
        return SuccessResult.of(deviceCodeDto);
    }

    private static final FailedResult LOGIN_PENDING = new FailedResult(-3001, "用户暂未完成登录");
    @PostMapping("/check_code/{aClient}/{aAccount}")
    public ResponseData onCheckDeviceCode(@PathVariable String aClient, @PathVariable String aAccount, String extraInfo) {
        AccountTokenDao account = accountModule.getAccount(aClient, aAccount);
        if (account == null) return FailedResult.NOT_FOUND;

        byte[] decode = Base64.getDecoder().decode(extraInfo.getBytes(StandardCharsets.UTF_8));
        ExtraInfo info = GsonUtil.fromJson(new String(decode, StandardCharsets.UTF_8), ExtraInfo.class);
        DeviceCodeCheckClient target = Feign.builder().target(DeviceCodeCheckClient.class, info.verificationUri);
        Map<String, Object> code = target.getTokenByDeviceCode(
                account.getParentClientId(),
                info.deviceCode,
                DeviceCodeCheckClient.GRANT_TYPE_AUTHOR
        );
        if (code.containsKey("error")) {
            if ("authorization_pending".equals(code.get("error"))) {
                return LOGIN_PENDING;
            }
            return new FailedResult(-3001, "Device Code 检查出错：" +
                    code.get("error_description"));
        }
        String json = GsonUtil.toJson(code);
        boolean check = accountModule.saveToken(aClient, aAccount,
                GsonUtil.fromJson(json, AccountTokenDto.class));
        return check ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }


    @PostMapping("/enabled/{aClient}/{aAccount}")
    public ResponseData enable(@PathVariable String aClient, @PathVariable String aAccount, Boolean enabled) {
        boolean setEnable = accountModule.enable(aClient, aAccount, enabled);
        return setEnable ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
    }

    @PostMapping("/delete/{aClient}/{aAccount}")
    public ResponseData delete(@PathVariable String aClient, @PathVariable String aAccount) {
        accountModule.delete(aClient, aAccount);
        return SuccessResult.SAMPLE;
    }
}
