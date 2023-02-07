package io.github.driveindex.azure.h2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.azure.h2.dao.AzureFileEntity;
import io.github.driveindex.azure.h2.repository.AzureCacheCentralMapper;
import io.github.driveindex.common.util.CanonicalPath;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author sgpublic
 * @Date 2022/8/15 21:00
 */
@Service
@RequiredArgsConstructor
public class AzureFileService extends ServiceImpl<AzureCacheCentralMapper, AzureFileEntity> {
    @Nullable
    public AzureFileEntity getByPath(CanonicalPath path) {
        return query()
                .eq("canonical_path_hash", path.hashCode())
                .one();
    }

    public Page<AzureFileEntity> pageByParentId(
            String parentId, Page<AzureFileEntity> page,
            @NonNull AzureFileEntity.Sort sort,
            @NonNull Boolean asc
    ) {
        return query().eq("parent_id", parentId)
                // 若按名称排序，则将文件夹排在前面
                .orderBy(sort.equals(AzureFileEntity.Sort.NAME), !asc, "is_dir")
                .orderBy(true, asc, sort.name().toLowerCase())
                .page(page);
    }

    @PostConstruct
    protected void init() {
        // TODO 启动定时追踪文件变更
    }
}
