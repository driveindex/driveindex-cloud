package io.github.driveindex.admin.controller;

import io.github.driveindex.admin.feign.AzureTokenClient;
import io.github.driveindex.admin.h2.dao.AccountTokenDto;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import io.github.driveindex.admin.module.AzureAccountModule;
import io.github.driveindex.admin.module.AzureClientModule;
import io.github.driveindex.admin.module.DriveConfigModule;
import io.github.driveindex.common.dto.admin.AzureAccountTokenDto;
import io.github.driveindex.common.dto.azure.drive.AzureDriveDto;
import io.github.driveindex.common.dto.azure.drive.AzureDriveListDto;
import io.github.driveindex.common.dto.result.FailedResult;
import io.github.driveindex.common.dto.result.ResponseData;
import io.github.driveindex.common.dto.result.SuccessResult;
import io.github.driveindex.common.exception.AzureDecodeException;
import io.github.driveindex.common.util.GsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/14 18:55
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AzureConfigController {
    private final AzureClientModule clientModule;
    private final AzureAccountModule accountModule;
    private final DriveConfigModule driveModule;
    private final AzureTokenClient tokenClient;

    public ResponseData listConfig() {
        AzureDriveListDto all = accountModule.getAll();
        for (AzureAccountTokenDto dto : all) {
            long expired = dto.getExpiresTime() - System.currentTimeMillis();
            try {
                if (expired < 10_000) refresh(dto);
            } catch (AzureDecodeException e) {
                log.warn("token 刷新失败！", e);
                return new FailedResult(-4101, "token 刷新失败：" + e.getMessage());
            }
        }
        return SuccessResult.of(all);
    }

    public ResponseData getConfig(String aClient, String aAccount, String aDrive) {
        if (StringUtils.hasText(aClient)) {
            return FailedResult.NOT_FOUND;
        }
        final AccountTokenDto accountDao;
        synchronized (accountModule) {
            if (StringUtils.hasText(aAccount)) {
                return FailedResult.NOT_FOUND;
            }
            accountDao = accountModule.getAccount(aClient, aAccount);
            if (accountDao == null) {
                return FailedResult.NOT_FOUND;
            }
            long expired = accountDao.getExpiresTime() - System.currentTimeMillis();
            try {
                if (expired < 10_000) refresh(accountDao);
            } catch (AzureDecodeException e) {
                log.warn("token 刷新失败！", e);
                return new FailedResult(-4101, "token 刷新失败：" + e.getMessage());
            }
        }
        final DriveConfigDao driveDao;
        if (StringUtils.hasText(aDrive)) {
            driveDao = driveModule.getDriveConfig(aClient, accountDao.getId(), aDrive);
        } else {
            driveDao = driveModule.getDefaultDriveConfig(aClient, accountDao.getId());
        }
        if (driveDao == null) {
            return FailedResult.NOT_FOUND;
        }
        AzureDriveDto result = new AzureDriveDto();
        result.setToken(createTokenDto(accountDao));
        result.setDirHome(driveDao.getDirHome());
        result.getCalledName().setPrefix(accountDao.getCalledName());
        result.getCalledName().setSuffix(driveDao.getCalledName());
        return SuccessResult.of(result);
    }

    private AzureAccountTokenDto createTokenDto(AccountTokenDto dao) {
        AzureAccountTokenDto dto = new AzureAccountTokenDto();
        dto.setAccessToken(dao.getAccessToken());
        dto.setTokenType(dao.getTokenType());
        return dto;
    }

    private void refresh(AzureAccountTokenDto dao) throws AzureDecodeException {
        AzureClientDao parentClient = clientModule.getById(dao.getParentClient());
        assert parentClient != null;
        io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto.Request request =
                new io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto.Request();
        request.setClientId(parentClient.getClientId());
//        request.setClientSecret(parentClient.getClientSecret());
        request.setGrantType(AzureTokenClient.GRANT_TYPE_REFRESH);
        request.setRefreshToken(dao.getRefreshToken());
        Map<String, Object> map = tokenClient.refreshToken(request);
        String json = GsonUtil.fromMap(map);
        AccountTokenDto token = GsonUtil.fromJson(json, AccountTokenDto.class);
        dao.setAccessToken(token.getAccessToken());
        dao.setRefreshToken(token.getRefreshToken());
        dao.setExpiresIn(token.getExpiresTime());
        dao.setScope(token.getScope());
        dao.setTokenType(token.getTokenType());
        accountModule.save(token);
    }
}
