package io.github.driveindex.admin.module;

import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.h2.service.AzureClientService;
import io.github.driveindex.common.dto.azure.drive.AzureClientDetailDto;
import io.github.driveindex.common.dto.azure.drive.AzureClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/9 12:12
 */
@RequiredArgsConstructor
@Component
public class AzureClientModule {
    private final AzureClientService client;
    private final AzureAccountModule accountModule;

    public LinkedList<AzureClientDto> getAll() {
        LinkedList<AzureClientDto> result = new LinkedList<>();
        List<AzureClientDao> all = client.getAll();
        for (AzureClientDao dao : all) {
            AzureClientDto tmp = new AzureClientDto();
            tmp.setId(dao.getId());
            tmp.setDetail(dao.clone());
            tmp.setChild(accountModule.getAll(dao.getId()));
            result.add(tmp);
        }
        if (!result.isEmpty()) result.getFirst().setIsDefault(true);
        return result;
    }

    @Nullable
    public AzureClientDao getById(@NonNull String id) {
        return client.getById(id);
    }

    public boolean save(String id, AzureClientDetailDto dto) {
        AzureClientDao dao = client.getById(id);
        boolean clientExist = client.clientExists(dto.getClientId());
        if (dao == null && clientExist) {
            // 若 id 不存在但 clientId 存在，则用户尝试重复创建 client，拦截此操作
            return false;
        }
        if (dao == null) {
            dao = new AzureClientDao();
            dao.setId(id);
            dao.setClientId(dto.getClientId());
        }
        dao.setCalledName(dto.getCalledName());
        dao.setClientId(dto.getClientId());
        dao.setEnable(dto.getEnable());
        dao.setDefaultTargetFlag(-System.currentTimeMillis());
        client.saveOrUpdate(dao);
        return true;
    }

    public void delete(String id) {
        client.removeById(id);
    }
}
