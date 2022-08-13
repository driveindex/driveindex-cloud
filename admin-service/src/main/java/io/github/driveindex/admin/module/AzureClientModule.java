package io.github.driveindex.admin.module;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.h2.repository.AzureClientRepository;
import io.github.driveindex.common.dto.azure.drive.AzureClientDetailDto;
import io.github.driveindex.common.dto.azure.drive.AzureClientDto;
import io.github.driveindex.common.util.Value;
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
            dao.setClientSecret(AzureClientDetailDto.PLACEHOLDER_CLIENT_SECRET);
            tmp.setDetail(dao.clone());
            tmp.setChild(accountModule.getAll(dao.getId()));
            result.add(tmp);
        }
        if (!result.isEmpty()) result.getFirst().setIsDefault(true);
        return result;
    }

    public AzureClientDao getById(@NonNull String id) {
        return client.getById(id);
    }

    public boolean save(String id, AzureClientDetailDto dto) {
        boolean idExist = client.selectById(id) != null;
        boolean clientExist = client.selectOne(new QueryWrapper<AzureClientDao>()
                .eq("client_id", dto.getClientId())) != null;
        if (!idExist && clientExist) {
            // 若 id 不存在但 clientId 存在，则用户尝试重复创建 client，拦截此操作
            return false;
        }
        if (idExist) {
            AzureClientDao exist = client.selectById(id);
            Value.check(dto.getCalledName(), (exist::setCalledName));
            String clientSecret = dto.getClientSecret();
            if (AzureClientDetailDto.PLACEHOLDER_CLIENT_SECRET.equals(clientSecret))
                clientSecret = null;
            Value.check(clientSecret, (exist::setClientSecret));
            client.updateById(exist);
        } else {
            client.insert(AzureClientDao.of(id, dto));
        }
        return true;
    }

    public boolean setEnabled(String id, Boolean enabled) {
        boolean idExist = client.exists(new QueryWrapper<AzureClientDao>()
                .eq("id", id));
        if (!idExist) return false;
        AzureClientDao exist = client.selectById(id);
        Value.check(enabled, (exist::setEnable));
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
        Value.check(System.currentTimeMillis(), (clientDao::setDefaultTargetFlag));
        client.updateById(clientDao);
        return true;
    }
}
