package io.github.driveindex.admin.h2.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:07
 */
@Mapper
public interface AzureClientRepository extends BaseMapper<AzureClientDao> {
    @Select("select * from `azure_client` order by `default_target_flag` desc")
    LinkedList<AzureClientDao> getAll();

    @Nullable
    @Select("select * from `azure_client` where `id`=#{id}")
    AzureClientDao getById(String id);
}
