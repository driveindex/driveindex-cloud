package io.github.driveindex.admin.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import io.github.driveindex.admin.h2.mapper.DriveConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/16 14:27
 */
@Service
public class DriveConfigService extends ServiceImpl<DriveConfigMapper, DriveConfigDao> {
    public List<DriveConfigDao> getByAccount(String aClient, String aAccount) {
        return query().allEq(Map.of("parent_client", aClient, "parent_account", aAccount))
                .orderBy(true, false, "default_target_flag").list();
    }

    public DriveConfigDao getDefaultByAccount(String aClient, String aAccount) {
        return query().allEq(Map.of("parent_client", aClient, "parent_account", aAccount))
                .orderBy(true, false, "default_target_flag")
                .one();
    }

    public DriveConfigDao getByConfig(String aClient, String aAccount, String aConfig) {
        return query().allEq(Map.of(
                "id", aConfig,
                "parent_client", aClient,
                "parent_account", aAccount
        )).one();
    }

    public boolean remove(String aClient, String aAccount, String aConfig) {
        return update().allEq(Map.of(
                "id", aConfig,
                "parent_client", aClient,
                "parent_account", aAccount
        )).remove();
    }
}