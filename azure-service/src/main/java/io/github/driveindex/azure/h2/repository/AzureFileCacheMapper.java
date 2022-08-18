package io.github.driveindex.azure.h2.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.azure.h2.dao.FileCacheEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sgpublic
 * @Date 2022/8/15 21:00
 */
@Mapper
public interface AzureFileCacheMapper extends BaseMapper<FileCacheEntity> {

}
