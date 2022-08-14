package io.github.driveindex.admin.h2.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:07
 */
@Mapper
public interface DriveConfigRepository extends BaseMapper<DriveConfigDao> {
    @Select("select * from `drive_config` where `parent_client`=#{aClient} and `parent_account`=#{aAccount} order by `default_target_flag` desc")
    LinkedList<DriveConfigDao> getByAccount(String aClient, String aAccount);

    @Nullable
    @Select("select * from `drive_config` where `parent_client`=#{aClient} and `parent_account`=#{aAccount} and `id`=#{aConfig}")
    DriveConfigDao getByConfig(String aClient, String aAccount, String aConfig);
}
