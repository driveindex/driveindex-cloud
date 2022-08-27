package io.github.driveindex.admin.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import io.github.driveindex.admin.h2.mapper.DriveConfigMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sgpublic
 * @Date 2022/8/16 14:27
 */
@Service
public class DriveConfigService extends ServiceImpl<DriveConfigMapper, DriveConfigDao> {
    public List<DriveConfigDao> getByAccount(@NonNull String aClient, @NonNull String aAccount) {
        return query()
                .allEq(Map.of(
                        "parent_client", aClient,
                        "parent_account", aAccount
                ))
                .orderBy(true, false, "default_target_flag")
                .list();
    }

    public Optional<DriveConfigDao> getDefaultByAccount(@NonNull String aClient, @NonNull String aAccount) {
        return query()
                .allEq(Map.of(
                        "parent_client", aClient,
                        "parent_account", aAccount
                ))
                .orderBy(true, false, "default_target_flag")
                .last("limit 1")
                .oneOpt();
    }

    public Optional<DriveConfigDao> getByConfig(@NonNull String aClient, @NonNull String aAccount, @NonNull String aConfig) {
        return query()
                .allEq(Map.of(
                        "id", aConfig,
                        "parent_client", aClient,
                        "parent_account", aAccount
                ))
                .oneOpt();
    }

    public boolean remove(@NonNull String aClient, @NonNull String aAccount, @NonNull String aConfig) {
        return update()
                .allEq(Map.of(
                        "id", aConfig,
                        "parent_client", aClient,
                        "parent_account", aAccount
                ))
                .remove();
    }
}