package io.github.driveindex.azure.h2.service;

import io.github.driveindex.azure.h2.repository.AzureDirCacheMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.azure.h2.dao.DirCacheEntity;
import io.github.driveindex.common.dto.azure.file.DirDetailDto;
import org.springframework.stereotype.Service;

/**
 * @author sgpublic
 * @Date 2022/8/15 21:00
 */
@Service
public class AzureDirCacheService extends ServiceImpl<AzureDirCacheMapper, DirCacheEntity> {
    public DirDetailDto getById(String id) {
        return query().eq("id", id).one().clone();
    }
}
