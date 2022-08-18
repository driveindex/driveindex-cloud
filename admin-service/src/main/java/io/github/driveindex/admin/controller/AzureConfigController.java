package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.admin.h2.dao.AccountTokenDao;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import io.github.driveindex.admin.module.AzureAccountModule;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.admin.module.DriveConfigModule;
import io.github.driveindex.common.DriveIndexCommon;
import io.github.driveindex.common.dto.azure.drive.AzureDriveDto;
import io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto;
import io.github.driveindex.common.dto.azure.microsoft.AzureTokenDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.github.driveindex.common.exception.AzureDecodeException;
import io.github.driveindex.common.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/14 18:55
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class AzureConfigController {
    private final AzureClientModule clientModule;
    private final AzureAccountModule accountModule;
    private final DriveConfigModule driveModule;
    private final AzureTokenClient tokenClient;

    @GetMapping("/config")
    public ResponseData getToken(
            @RequestParam(required = false) String client,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String drive
    ) {
        if (client == null || client.isBlank()) {
            AzureClientDao defaultClient = clientModule.getDefault();
            if (defaultClient == null) {
                return FailedResult.NOT_FOUND;
            }
            client = defaultClient.getId();
        }
        final AccountTokenDao accountDao;
        synchronized (accountModule) {
            if (account == null || account.isBlank()) {
                accountDao = accountModule.getDefault(client);
            } else {
                accountDao = accountModule.getAccount(client, account);
            }
            if (accountDao == null) {
                return FailedResult.NOT_FOUND;
            }
            long expired = accountDao.getExpiresIn() - System.currentTimeMillis();
            try {
                if (expired < 10_000) refresh(accountDao);
            } catch (AzureDecodeException e) {
                log.warn("token 刷新失败！", e);
                return new FailedResult(-4001, "token 刷新失败：" + e.getMessage());
            }
        }
        final DriveConfigDao driveDao;
        if (drive != null) {
            driveDao = driveModule.getDriveConfig(client, account, drive);
        } else {
            driveDao = driveModule.getDefaultDriveConfig(client, account);
        }
        AzureDriveDto result = new AzureDriveDto();
        result.setToken(createTokenDto(accountDao));
        if (driveDao == null) {
            result.setDirHome("");
            result.getCalledName().setPrefix(DriveIndexCommon.APPLICATION_BASE_NAME);
            result.getCalledName().setSuffix(accountDao.getCalledName());
        } else {
            result.setDirHome(driveDao.getDirHome());
            result.getCalledName().setPrefix(accountDao.getCalledName());
            result.getCalledName().setSuffix(driveDao.getCalledName());
        }
        return SuccessResult.of(result);
    }

    private AzureTokenDto createTokenDto(AccountTokenDao dao) {
        AzureTokenDto dto = new AzureTokenDto();
        dto.setAccessToken(dao.getAccessToken());
        dto.setTokenType(dao.getTokenType());
        return dto;
    }

    private void refresh(AccountTokenDao dao) throws AzureDecodeException {
        AzureClientDao parentClient = clientModule.getById(dao.getParentClient());
        assert parentClient != null;
        AccountTokenDto.Request request = new AccountTokenDto.Request();
        request.setClientId(parentClient.getClientId());
//        request.setClientSecret(parentClient.getClientSecret());
        request.setGrantType(AzureTokenClient.GRANT_TYPE_REFRESH);
        request.setRefreshToken(dao.getRefreshToken());
        Map<String, Object> map = tokenClient.refreshToken(request);
        String json = GsonUtil.fromMap(map);
        AccountTokenDto.Response token = GsonUtil.fromJson(json, AccountTokenDto.Response.class);
        dao.setAccessToken(token.getAccessToken());
        dao.setRefreshToken(token.getRefreshToken());
        dao.setExpiresIn(token.getExpiresTime());
        dao.setScope(token.getScope());
        dao.setTokenType(token.getTokenType());
        accountModule.save(dao);
    }
}
