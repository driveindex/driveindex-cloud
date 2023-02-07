package io.github.driveindex.azure.h2.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.azure.h2.dao.AzureDeltaEntity;
import io.github.driveindex.azure.h2.dao.ContentCacheEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sgpublic
 * @Date 2023/2/7 10:10
 */
@Mapper
public interface AzureDeltaMapper extends BaseMapper<AzureDeltaEntity> {
}
