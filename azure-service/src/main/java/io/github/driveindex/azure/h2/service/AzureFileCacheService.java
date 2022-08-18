package io.github.driveindex.azure.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.azure.h2.dao.FileCacheEntity;
import io.github.driveindex.azure.h2.repository.AzureFileCacheMapper;
import io.github.driveindex.common.dto.azure.file.FileDetailDto;
import org.springframework.stereotype.Service;

/**
 * @author sgpublic
 * @Date 2022/8/15 21:00
 */
@Service
public class AzureFileCacheService extends ServiceImpl<AzureFileCacheMapper, FileCacheEntity> {
    public FileDetailDto getById(String id) {
        return super.getById(id).clone();
    }

    public String getDownloadUrlById(String id) {
        return super.getById(id).getDownloadUrl();
    }
}
