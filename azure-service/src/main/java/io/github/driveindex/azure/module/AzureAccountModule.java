package io.github.driveindex.azure.module;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.driveindex.azure.h2.dao.AccountTokenDao;
import io.github.driveindex.azure.h2.dao.AzureClientDao;
import io.github.driveindex.azure.h2.repository.AccountTokenRepository;
import io.github.driveindex.azure.h2.repository.AzureClientRepository;
import io.github.driveindex.common.dto.azure.common.AccountTokenDto;
import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
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
public class AzureAccountModule {
    private final AzureClientRepository client;
    private final AccountTokenRepository token;
    private final DriveConfigModule drive;

    public LinkedList<io.github.driveindex.common.dto.azure.drive.AccountDto> getAll(String aClient) {
        LinkedList<io.github.driveindex.common.dto.azure.drive.AccountDto> result = new LinkedList<>();
        LinkedList<AccountTokenDao> accounts = token.getByClientId(aClient);
        for (AccountTokenDao account : accounts) {
            io.github.driveindex.common.dto.azure.drive.AccountDto tmp = new io.github.driveindex.common.dto.azure.drive.AccountDto();
            tmp.setId(account.getId());
            tmp.setDetail(account);
            tmp.setChild(drive.getAll(aClient, account.getId()));
            result.add(tmp);
        }
        return result;
    }

    @Nullable
    public AccountTokenDao getAccount(String aClient, String aAccount) {
        return token.getByAccount(aClient, aAccount);
    }

    public boolean save(String aClient, String aAccount, AccountDetailDto dto) {
        AccountTokenDao account = getAccount(aClient, aAccount);
        if (account == null) return false;
        boolean clientExist = client.exists(new QueryWrapper<AzureClientDao>().eq("id", aClient));
        if (!clientExist) return false;
        Value.check(dto.getCalledName(), (account::setCalledName));
        token.updateById(account);
        return true;
    }

    public boolean saveToken(String aClient, String aAccount, AccountTokenDto tokenDto) {
        AccountTokenDao account = getAccount(aClient, aAccount);
        if (account == null) return false;

        Value.check(tokenDto.getTokenType(), (account::setTokenType));
        Value.check(tokenDto.getAccessToken(), (account::setAccessToken));
        Value.check(tokenDto.getRefreshToken(), (account::setRefreshToken));
        Value.check(tokenDto.getIdToken(), (account::setIdToken));
        Value.check(tokenDto.getScope(), (account::setScope));
        Value.check(tokenDto.getExpiresIn(), (account::setExpiresIn));

        token.updateById(account);
        return true;
    }

    public boolean enable(String aClient, String aAccount, boolean enabled) {
        AccountTokenDao account = getAccount(aClient, aAccount);
        if (account == null) return false;

        AccountTokenDao exist = token.getByAccount(aClient, aAccount);
        exist.setEnable(enabled);
        token.updateById(exist);
        return true;
    }

    public void delete(String aClient, String aAccount) {
        token.delete(new QueryWrapper<AccountTokenDao>().allEq(Map.of(
                "id", aAccount,
                "parent_client", aClient
        )));
        drive.delete(aClient, aAccount);
    }

    public void delete(String aClient) {
        token.delete(new QueryWrapper<AccountTokenDao>().allEq(Map.of(
                "parent_client", aClient
        )));
        drive.delete(aClient);
    }
}
