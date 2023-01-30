package io.github.driveindex.admin.module;

import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import io.github.driveindex.admin.h2.service.AccountTokenService;
import io.github.driveindex.admin.h2.service.DriveConfigService;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDetailDto;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDto;
import io.github.driveindex.common.util.CanonicalPath;
import io.github.driveindex.common.util.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
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

    public LinkedList<DriveConfigDto> getAll(@NonNull String aClient, @NonNull String aAccount) {
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
    public DriveConfigDao getDefaultDriveConfig(@NonNull String aClient, @NonNull String aAccount) {
        if (!config.getByAccount(aClient, aAccount).isEmpty()) {
            return null;
        }
        DriveConfigDao dao = new DriveConfigDao();
        dao.setId(DriveConfigDao.DEFAULT_ID);
        dao.setParentAccount(aAccount);
        dao.setParentClient(aClient);
        dao.setDirHome(CanonicalPath.ROOT_PATH);
        return dao;
    }

    @Nullable
    public DriveConfigDao getDriveConfig(@NonNull String aClient, @NonNull String aAccount, @NonNull String aConfig) {
        return config.getByConfig(aClient, aAccount, aConfig).orElse(null);
    }

    public boolean save(@NonNull String aClient, @NonNull String aAccount, @NonNull String aConfig, @NonNull DriveConfigDetailDto dto) {
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

    public boolean enable(@NonNull String aClient, @NonNull String aAccount, @NonNull String aConfig, @NonNull Boolean enabled) {
        DriveConfigDao dao = getDriveConfig(aClient, aAccount, aConfig);
        if (dao == null) return false;
        Value.check(enabled, (dao::setEnable));
        config.updateById(dao);
        return true;
    }

    public void delete(@NonNull String aClient, @NonNull String aAccount, @NonNull String aConfig) {
        config.remove(aClient, aAccount, aConfig);
    }
}
