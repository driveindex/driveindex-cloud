package io.github.driveindex.admin.module;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.driveindex.admin.h2.dao.AccountTokenDao;
import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import io.github.driveindex.admin.h2.repository.AccountTokenRepository;
import io.github.driveindex.admin.h2.repository.DriveConfigRepository;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDetailDto;
import io.github.driveindex.common.dto.azure.drive.DriveConfigDto;
import io.github.driveindex.common.util.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:12
 */
@RequiredArgsConstructor
@Component
public class DriveConfigModule {
    private final AccountTokenRepository token;
    private final DriveConfigRepository config;

    public LinkedList<DriveConfigDto> getAll(String aClient, String aAccount) {
        LinkedList<DriveConfigDto> result = new LinkedList<>();
        LinkedList<DriveConfigDao> drives = config.getByAccount(aClient, aAccount);
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
    public DriveConfigDao getDriveConfig(String aClient, String aAccount, String aConfig) {
        return config.getByConfig(aClient, aAccount, aConfig);
    }

    public boolean save(String aClient, String aAccount, String aConfig, DriveConfigDetailDto dto) {
        DriveConfigDao dao = getDriveConfig(aClient, aAccount, aConfig);
        if (dao == null) {
            boolean accountExist = token.selectOne(new QueryWrapper<AccountTokenDao>().allEq(
                    Map.of("parent_client", aClient, "id", aAccount)
            )) != null;
            if (!accountExist) return false;
            dao = new DriveConfigDao();
            Value.check(aConfig, (dao::setId));
            Value.check(aClient, (dao::setParentClient));
            Value.check(aAccount, (dao::setParentAccount));
            Value.check(dto.getCalledName(), (dao::setCalledName));
            Value.check(dto.getDirHome(), (dao::setDirHome));
            Value.check(dto.getEnable(), (dao::setEnable));
            config.insert(dao);
        } else {
            Value.check(dto.getCalledName(), (dao::setCalledName));
            Value.check(dto.getDirHome(), (dao::setDirHome));
            Value.check(dto.getEnable(), (dao::setEnable));
            config.updateById(dao);
        }

        return true;
    }

    public boolean enable(String aClient, String aAccount, String aConfig, Boolean enabled) {
        DriveConfigDao dao = getDriveConfig(aClient, aAccount, aConfig);
        if (dao == null) return false;
        Value.check(enabled, (dao::setEnable));
        config.updateById(dao);
        return true;
    }

    public void delete(String aClient, String aAccount, String aConfig) {
        config.delete(new QueryWrapper<DriveConfigDao>().allEq(Map.of(
                "id", aConfig,
                "parent_client", aClient,
                "parent_account", aAccount
        )));
    }

    public void delete(String aClient, String aAccount) {
        config.delete(new QueryWrapper<DriveConfigDao>().allEq(Map.of(
                "parent_client", aClient,
                "parent_account", aAccount
        )));
    }

    public void delete(String aClient) {
        config.delete(new QueryWrapper<DriveConfigDao>().eq("parent_client", aClient));
    }

    public boolean setDefault(String aClient, String aAccount, String aConfig) {
        DriveConfigDao configDao = getDriveConfig(aClient, aAccount, aConfig);
        if (configDao == null) return false;
        configDao.setDefaultTargetFlag(System.currentTimeMillis());
        config.updateById(configDao);
        return true;
    }
}
