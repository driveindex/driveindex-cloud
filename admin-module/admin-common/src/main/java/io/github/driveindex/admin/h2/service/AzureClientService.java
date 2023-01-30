package io.github.driveindex.admin.h2.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import io.github.driveindex.admin.h2.mapper.AzureClientMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/16 14:27
 */
@Service
public class AzureClientService extends ServiceImpl<AzureClientMapper, AzureClientDao> {
    public List<AzureClientDao> getAll() {
        return list();
    }

    public boolean clientExists(@NonNull String aClient) {
        return query().eq("id", aClient).exists();
    }
}