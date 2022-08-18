package io.github.driveindex.azure.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.azure.h2.dao.ContentCacheEntity;
import io.github.driveindex.azure.h2.repository.AzureContentCacheMapper;
import org.springframework.stereotype.Service;

/**
 * @author sgpublic
 * @Date 2022/8/15 21:00
 */
@Service
public class AzureContentCacheService extends ServiceImpl<AzureContentCacheMapper, ContentCacheEntity> {
    public String getContentById(String id) {
        return super.getById(id).getContent();
    }
}
