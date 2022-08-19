package io.github.driveindex.azure.h2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.azure.h2.dao.CacheCentralEntity;
import io.github.driveindex.azure.h2.repository.AzureCacheCentralMapper;
import io.github.driveindex.common.manager.ConfigManager;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author sgpublic
 * @Date 2022/8/15 21:00
 */
@Service
@RequiredArgsConstructor
public class AzureCacheCentralService extends ServiceImpl<AzureCacheCentralMapper, CacheCentralEntity> {
    @Nullable
    public CacheCentralEntity getByPath(String path) {
        return query().eq("canonical_path_hash", path.toLowerCase().hashCode())
                .gt(true, "expires_time", System.currentTimeMillis())
                .one();
    }

    public Page<CacheCentralEntity> pageByParentId(
            String parentId, Page<CacheCentralEntity> page,
            @NonNull CacheCentralEntity.Sort sort,
            @NonNull Boolean asc
    ) {
        return query().eq("parent_id", parentId)
                .gt(true, "expires_time", System.currentTimeMillis())
                .orderBy(true, asc, sort.name().toLowerCase())
                .page(page);
    }

    private final ThreadPoolTaskScheduler cacheCleanerScheduler;
    @PostConstruct
    protected void init() {
        // 设置定时清理过期缓存任务，启动后 5 秒开始执行
        cacheCleanerScheduler.scheduleWithFixedDelay(
                () -> {
                    update().le(true, "expires_time", System.currentTimeMillis())
                            .remove();
                    log.debug("清理过期记录。");
                },
                new Date(System.currentTimeMillis() + 5_000),
                ConfigManager.getCacheCleanTickTime() * 1000L
        );
    }
}
