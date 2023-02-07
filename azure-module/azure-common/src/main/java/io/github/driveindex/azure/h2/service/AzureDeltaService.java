package io.github.driveindex.azure.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.azure.h2.dao.AzureDeltaEntity;
import io.github.driveindex.azure.h2.repository.AzureDeltaMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author sgpublic
 * @Date 2022/8/15 21:00
 */
@Service
public class AzureDeltaService extends ServiceImpl<AzureDeltaMapper, AzureDeltaEntity> {
    // https://learn.microsoft.com/zh-cn/graph/api/driveitem-delta?view=graph-rest-1.0&tabs=http
    private static final String DeltaStarter = "https://graph.microsoft.com/v1.0/me/drive/root/delta";

    public String getDeltaUrl(String aClient, String aAccount) {
        Optional<AzureDeltaEntity> entity = query()
                .eq("client_id", aClient)
                .eq("account_id", aAccount)
                .oneOpt();
        return entity.isEmpty() ? DeltaStarter : entity.get().getDeltaUrl();
    }

    public void setDeltaUrl(String aClient, String aAccount, String nextDelta) {
        saveOrUpdate(new AzureDeltaEntity(aClient, aAccount, nextDelta));
    }

    public boolean delete(String aClient, String aAccount) {
        return update()
                .eq("client_id", aClient)
                .eq("account_id", aAccount)
                .remove();
    }
}
