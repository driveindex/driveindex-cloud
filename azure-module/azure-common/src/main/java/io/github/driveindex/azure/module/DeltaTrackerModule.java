package io.github.driveindex.azure.module;

import io.github.driveindex.azure.h2.dao.AzureDeltaEntity;
import io.github.driveindex.azure.h2.service.AzureDeltaService;
import io.github.driveindex.common.dto.admin.AzureAccountTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author sgpublic
 * @Date 2023/2/7 9:58
 */
@Component
@RequiredArgsConstructor
public class DeltaTrackerModule {
    private final Queue<Runnable> tracker = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    protected void init() {
        executor.execute(() -> {
            while (true) {
                if (tracker.isEmpty()) continue;
                tracker.poll().run();
            }
        });
        tracker.add(this::run);
    }

    private final DriveConfigModule configModule;
    private final AzureDeltaService deltaService;
    private void run() {
        List<AzureDeltaEntity> remoteList = new ArrayList<>();
        for (AzureAccountTokenDto account : configModule.listConfig()) {
            AzureDeltaEntity delta = new AzureDeltaEntity();
            delta.setAccountId(account.getId());
            delta.setClientId(account.getParentClient());
            delta.setDeltaUrl(delta.getDeltaUrl());
        }
        List<AzureDeltaEntity> deltaList = deltaService.list();
    }
}
