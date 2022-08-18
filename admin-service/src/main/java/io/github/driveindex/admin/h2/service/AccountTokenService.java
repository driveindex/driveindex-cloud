package io.github.driveindex.admin.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.admin.h2.dao.AccountTokenDao;
import io.github.driveindex.admin.h2.mapper.AccountTokenMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/16 14:27
 */
@Service
public class AccountTokenService extends ServiceImpl<AccountTokenMapper, AccountTokenDao> {
    public List<AccountTokenDao> getByClientId(String aClient) {
        return query().eq("parent_client", aClient)
                .orderBy(true, false, "default_target_flag").list();
    }

    public AccountTokenDao getDefaultByClientId(String aClient) {
        return query().eq("parent_client", aClient)
                .orderBy(true, false, "default_target_flag")
                .one();
    }

    public AccountTokenDao getByAccount(String aClient, String aAccount) {
        return query().allEq(Map.of("parent_client", aClient, "id", aAccount)).one();
    }

    public boolean exists(String aClient, String aAccount) {
        return query().allEq(Map.of(
                "id", aAccount,
                "parent_client", aClient
        )).exists();
    }
}