package io.github.driveindex.admin.module;

import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import io.github.driveindex.admin.h2.service.AccountTokenService;
import io.github.driveindex.admin.h2.service.DriveConfigService;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDetailDto;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDto;
import io.github.driveindex.common.util.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:12
 */
@RequiredArgsConstructor
@Component
public class DriveConfigModule {
    private final AccountTokenService token;
    private final DriveConfigService config;

    public LinkedList<DriveConfigDto> getAll(String aClient, String aAccount) {
        LinkedList<DriveConfigDto> result = new LinkedList<>();
        List<DriveConfigDao> drives = config.getByAccount(aClient, aAccount);
        for (DriveConfigDao drive : drives) {
            DriveConfigDto tmp = new DriveConfigDto();
            tmp.setId(drive.getId());
            tmp.setDetail(drive.clone());
            result.add(tmp);
        }
        if (!result.isEmpty()) result.getFirst().setIsDefault(true);
        return result;
    }

    @Nullable
    public DriveConfigDao getDefaultDriveConfig(String aClient, String aAccount) {
        return config.getDefaultByAccount(aClient, aAccount);
    }

    @Nullable
    public DriveConfigDao getDriveConfig(String aClient, String aAccount, String aConfig) {
        return config.getByConfig(aClient, aAccount, aConfig);
    }

    public boolean save(String aClient, String aAccount, String aConfig, DriveConfigDetailDto dto) {
        DriveConfigDao dao = getDriveConfig(aClient, aAccount, aConfig);
        if (dao == null) {
            boolean accountExist = token.exists(aClient, aAccount);
            if (!accountExist) return false;
            dao = new DriveConfigDao();
            Value.check(aConfig, (dao::setId));
            Value.check(aClient, (dao::setParentClient));
            Value.check(aAccount, (dao::setParentAccount));
        }
        Value.check(dto.getCalledName(), (dao::setCalledName));
        Value.check(dto.getCanonicalDirHome(), dao::setCanonicalDirHome);
        Value.check(dto.getEnable(), (dao::setEnable));
        config.saveOrUpdate(dao);

        return true;
    }

    public boolean enable(String aClient, String aAccount, String aConfig, Boolean enabled) {
        DriveConfigDao dao = getDriveConfig(aClient, aAccount, aConfig);
        if (dao == null) return false;
        Value.check(enabled, (dao::setEnable));
        config.updateById(dao);
        return true;
    }

    public boolean delete(String aClient, String aAccount, String aConfig) {
        return config.remove(aClient, aAccount, aConfig);
    }

    public boolean setDefault(String aClient, String aAccount, String aConfig) {
        DriveConfigDao configDao = getDriveConfig(aClient, aAccount, aConfig);
        if (configDao == null) return false;
        configDao.setDefaultTargetFlag(System.currentTimeMillis());
        config.updateById(configDao);
        return true;
    }
}
