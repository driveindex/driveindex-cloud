package io.github.driveindex.admin.module;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.driveindex.admin.h2.dao.AccountTokenDao;
import io.github.driveindex.admin.h2.service.AccountTokenService;
import io.github.driveindex.admin.h2.service.AzureClientService;
import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
import io.github.driveindex.common.dto.azure.drive.AccountDto;
import io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto;
import io.github.driveindex.common.util.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:12
 */
@RequiredArgsConstructor
@Component
public class AzureAccountModule {
    private final AzureClientService client;
    private final AccountTokenService token;
    private final DriveConfigModule drive;

    public LinkedList<AccountDto> getAll(String aClient) {
        LinkedList<AccountDto> result = new LinkedList<>();
        List<AccountTokenDao> accounts = token.getByClientId(aClient);
        for (AccountTokenDao account : accounts) {
            AccountDto tmp = new AccountDto();
            tmp.setId(account.getId());
            tmp.setDetail(account.clone());
            tmp.setChild(drive.getAll(aClient, account.getId()));
            result.add(tmp);
        }
        if (!result.isEmpty()) result.getFirst().setIsDefault(true);
        return result;
    }

    @Nullable
    public AccountTokenDao getDefault(String aClient) {
        return token.getDefaultByClientId(aClient);
    }

    @Nullable
    public AccountTokenDao getAccount(String aClient, String aAccount) {
        return token.getByAccount(aClient, aAccount);
    }

    public boolean save(String aClient, String aAccount, AccountDetailDto dto) {
        AccountTokenDao account = getAccount(aClient, aAccount);
        if (account == null) {
            boolean clientExist = client.getById(aClient) != null;
            if (!clientExist) return false;
            account = new AccountTokenDao();
            Value.check(aAccount, (account::setId));
            Value.check(aClient, (account::setParentClient));
        }
        Value.check(dto.getCalledName(), (account::setCalledName));
        Value.check(dto.getEnable(), (account::setEnable));
        token.saveOrUpdate(account);
        return true;
    }

    public boolean saveToken(String aClient, String aAccount, AccountTokenDto.Response tokenDto) {
        AccountTokenDao account = getAccount(aClient, aAccount);
        if (account == null) return false;

        Value.check(tokenDto.getTokenType(), (account::setTokenType));
        Value.check(tokenDto.getAccessToken(), (account::setAccessToken));
        Value.check(tokenDto.getRefreshToken(), (account::setRefreshToken));
        Value.check(tokenDto.getScope(), (account::setScope));
        Value.check(tokenDto.getExpiresIn(), (value -> account.setExpiresIn(tokenDto.getExpiresTime())));
        account.setNeedLogin(false);
        token.updateById(account);
        return true;
    }

    public void save(AccountTokenDao dao) {
        if (getAccount(dao.getParentClient(), dao.getId()) == null) return;
        token.saveOrUpdate(dao);
    }

    public boolean enable(String aClient, String aAccount, Boolean enabled) {
        AccountTokenDao account = getAccount(aClient, aAccount);
        if (account == null) return false;

        AccountTokenDao exist = token.getByAccount(aClient, aAccount);
        Value.check(enabled, (exist::setEnable));
        token.updateById(exist);
        return true;
    }

    public void delete(String aClient, String aAccount) {
        token.remove(new QueryWrapper<AccountTokenDao>().allEq(Map.of(
                "id", aAccount,
                "parent_client", aClient
        )));
    }

    public boolean setDefault(String aClient, String aAccount) {
        AccountTokenDao account = getAccount(aClient, aAccount);
        if (account == null) return false;
        Value.check(System.currentTimeMillis(), (account::setDefaultTargetFlag));
        token.updateById(account);
        return true;
    }
}
