package io.github.driveindex.admin.module;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.h2.repository.AzureClientRepository;
import io.github.driveindex.common.dto.azure.drive.AccountDto;
import io.github.driveindex.common.dto.azure.drive.AzureClientDetailDto;
import io.github.driveindex.common.dto.azure.drive.AzureClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:12
 */
@RequiredArgsConstructor
@Component
public class AzureClientModule {
    private final AzureClientRepository client;
    private final AzureAccountModule accountModule;

    public LinkedList<AzureClientDto> getAll() {
        LinkedList<AzureClientDto> result = new LinkedList<>();
        LinkedList<AzureClientDao> all = client.getAll();
        for (AzureClientDao dao : all) {
            AzureClientDto tmp = new AzureClientDto();
            tmp.setId(dao.getId());
            tmp.setDetail(dao);
            LinkedList<AccountDto> accountByClient = accountModule.getAll(dao.getId());
            tmp.setChild(accountByClient);
            result.add(tmp);
        }
        return result;
    }

    public AzureClientDao getById(@NonNull String id) {
        return client.getById(id);
    }

    public boolean save(String id, AzureClientDetailDto dto) {
        boolean idExist = client.exists(new QueryWrapper<AzureClientDao>()
                .eq("id", id));
        boolean clientExist = client.exists(new QueryWrapper<AzureClientDao>()
                .eq("client_id", dto.getClientId()));
        if (!idExist && clientExist) {
            // 若 id 不存在但 clientId 存在，则用户尝试重复创建 client，拦截此操作
            return false;
        }
        if (idExist) {
            AzureClientDao exist = client.selectById(id);
            if (dto.getCalledName() != null) exist.setCalledName(dto.getCalledName());
            if (dto.getClientSecret() != null) exist.setClientSecret(dto.getClientSecret());
            client.updateById(exist);
        } else {
            client.insert(AzureClientDao.of(id, dto));
        }
        return true;
    }

    public boolean setEnabled(String id, boolean enabled) {
        boolean idExist = client.exists(new QueryWrapper<AzureClientDao>()
                .eq("id", id));
        if (!idExist) return false;
        AzureClientDao exist = client.selectById(id);
        exist.setEnable(enabled);
        client.updateById(exist);
        return true;
    }

    public void delete(String id) {
        client.delete(new QueryWrapper<AzureClientDao>().eq("id", id));
        accountModule.delete(id);
    }

    public boolean setDefault(String aClient) {
        AzureClientDao clientDao = getById(aClient);
        if (clientDao == null) return false;
        clientDao.setDefaultTargetFlag(System.currentTimeMillis());
        client.updateById(clientDao);
        return true;
    }
}
