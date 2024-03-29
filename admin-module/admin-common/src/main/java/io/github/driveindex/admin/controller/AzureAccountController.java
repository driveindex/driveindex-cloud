package io.github.driveindex.admin.controller;

import feign.codec.DecodeException;
import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.admin.h2.dao.AccountTokenDto;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.module.AzureAccountModule;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
import io.github.driveindex.common.dto.azure.drive.DeviceCodeCheckDto;
import io.github.driveindex.common.dto.azure.microsoft.DeviceCodeDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.github.driveindex.common.exception.AzureDecodeException;
import io.github.driveindex.common.util.GsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/8 13:34
 */
@Tag(name = "微软账号配置")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AzureAccountController {
    private final AzureClientModule clientModule;
    private final AzureAccountModule accountModule;
    private final AzureTokenClient feign;

    @Operation(summary = "创建账号配置", description = "新建一个账号配置")
    @PostMapping("/api/admin/azure_account/{aClient}/{aAccount}")
    public ResponseData saveAccount(
            @Schema(description = "Client 配置 ID")
            @PathVariable String aClient,
            @Schema(description = "账号配置 ID")
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


    @Operation(
            summary = "获取登陆 DeviceCode", description = "获取用户用于登陆微软账号所需的 DeviceCode",
            responses = @ApiResponse(content = @Content(schema = @Schema(
                    implementation = DeviceCodeDto.Response.class
            )))
    )
    @GetMapping("/api/admin/azure_account/device_code/{aClient}/{aAccount}")
    public ResponseData onDeviceCode(
            @Schema(description = "Client 配置 ID")
            @PathVariable String aClient,
            @Schema(description = "账号配置 ID")
            @PathVariable String aAccount
    ) {
        AccountTokenDto account = accountModule.getAccount(aClient, aAccount);
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
            log.warn("Device Code 申请出错！", e);
            return new FailedResult(-3002, "Device Code 申请出错：" + e.getMessage());
        }
    }

    private static final FailedResult LOGIN_PENDING = new FailedResult(-3001, "用户暂未完成登录");

    @Operation(summary = "检查 DeviceCode 结果", description = "检查目标 DeviceCode 操作结果")
    @PostMapping("/api/admin/azure_account/check_code/{aClient}/{aAccount}")
    public ResponseData onCheckDeviceCode(
            @Schema(description = "Client 配置 ID")
            @PathVariable String aClient,
            @Schema(description = "账号配置 ID")
            @PathVariable String aAccount,
            @RequestBody DeviceCodeCheckDto dto
    ) {
        AccountTokenDto account = accountModule.getAccount(aClient, aAccount);
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
                    GsonUtil.fromJson(json, io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto.Response.class));
            return saved ? SuccessResult.SAMPLE : FailedResult.NOT_FOUND;
        } catch (AzureDecodeException e) {
            if ("authorization_pending".equals(e.getCode())) return LOGIN_PENDING;
            log.warn("Device Code 检查出错！", e);
            return new FailedResult(-3003, "Device Code 检查出错：" + e.getMessage());
        }
    }

    @Operation(summary = "删除账号配置", description = "删除一个账号配置")
    @PostMapping("/api/admin/azure_account/delete/{aClient}/{aAccount}")
    public ResponseData delete(
            @Schema(description = "Client 配置 ID")
            @PathVariable String aClient,
            @Schema(description = "账号配置 ID")
            @PathVariable String aAccount
    ) {
        accountModule.delete(aClient, aAccount);
        return SuccessResult.SAMPLE;
    }
}
