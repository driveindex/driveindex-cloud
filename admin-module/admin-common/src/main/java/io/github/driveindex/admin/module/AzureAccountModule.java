package io.github.driveindex.admin.module;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.driveindex.admin.h2.dao.AccountTokenDto;
import io.github.driveindex.admin.h2.service.AccountTokenService;
import io.github.driveindex.admin.h2.service.AzureClientService;
import io.github.driveindex.common.dto.azure.drive.AccountDetailDto;
import io.github.driveindex.common.dto.azure.drive.AccountDto;
import io.github.driveindex.common.dto.azure.drive.AzureDriveListDto;
import io.github.driveindex.common.util.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
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

    public AzureDriveListDto getAll() {
        AzureDriveListDto result = new AzureDriveListDto();
        result.addAll(token.getAll());
        return result;
    }

    public LinkedList<AccountDto> getAllByClient(@NonNull String aClient) {
        LinkedList<AccountDto> result = new LinkedList<>();
        List<AccountTokenDto> accounts = token.getByClientId(aClient);
        for (AccountTokenDto account : accounts) {
            AccountDto tmp = new AccountDto();
            tmp.setId(account.getId());
            tmp.setDetail(account.clone());
            tmp.setChild(drive.getAll(aClient, account.getId()));
            result.add(tmp);
        }
        return result;
    }

    @Nullable
    public AccountTokenDto getAccount(@NonNull String aClient, @NonNull String aAccount) {
        return token.getByAccount(aClient, aAccount).orElse(null);
    }

    public boolean save(@NonNull String aClient, @NonNull String aAccount, @NonNull AccountDetailDto dto) {
        AccountTokenDto account = getAccount(aClient, aAccount);
        if (account == null) {
            boolean clientExist = client.getById(aClient) != null;
            if (!clientExist) return false;
            account = new AccountTokenDto();
            Value.check(aAccount, (account::setId));
            Value.check(aClient, (account::setParentClient));
        }
        Value.check(dto.getCalledName(), (account::setCalledName));
        Value.check(dto.getEnable(), (account::setEnable));
        token.saveOrUpdate(account);
        return true;
    }

    public boolean saveToken(@NonNull String aClient, @NonNull String aAccount, @NonNull io.github.driveindex.common.dto.azure.microsoft.AccountTokenDto.Response tokenDto) {
        AccountTokenDto account = getAccount(aClient, aAccount);
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

    public void save(@NonNull AccountTokenDto dao) {
        if (getAccount(dao.getParentClient(), dao.getId()) == null) return;
        token.saveOrUpdate(dao);
    }

    public boolean enable(@NonNull String aClient, @NonNull String aAccount, Boolean enabled) {
        AccountTokenDto account = getAccount(aClient, aAccount);
        if (account == null) return false;
        Value.check(enabled, (account::setEnable));
        token.updateById(account);
        return true;
    }

    public void delete(String aClient, String aAccount) {
        token.remove(new QueryWrapper<AccountTokenDto>().allEq(Map.of(
                "id", aAccount,
                "parent_client", aClient
        )));
    }
}
