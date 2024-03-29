package io.github.driveindex.admin.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.admin.h2.dao.AccountTokenDto;
import io.github.driveindex.admin.h2.mapper.AccountTokenMapper;
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
public class AccountTokenService extends ServiceImpl<AccountTokenMapper, AccountTokenDto> {
    public List<AccountTokenDto> getAll() {
        return query().list();
    }

    public List<AccountTokenDto> getByClientId(@NonNull String aClient) {
        return query().eq("parent_client", aClient).list();
    }

    public Optional<AccountTokenDto> getByAccount(@NonNull String aClient, @NonNull String aAccount) {
        return query()
                .allEq(Map.of(
                        "parent_client", aClient,
                        "id", aAccount
                ))
                .oneOpt();
    }

    public boolean exists(@NonNull String aClient, @NonNull String aAccount) {
        return query()
                .allEq(Map.of(
                        "id", aAccount,
                        "parent_client", aClient
                ))
                .exists();
    }
}