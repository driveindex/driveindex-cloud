package io.github.driveindex.admin.h2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.admin.h2.dao.DriveConfigDao;
import kotlin.Deprecated;
import kotlin.ReplaceWith;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:07
 */
@Mapper
public interface DriveConfigMapper extends BaseMapper<DriveConfigDao> {
    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getByAccount(aClient, aAccount)",
                    imports = { "io.github.driveindex.admin.h2.service.DriveConfigService" }
            )
    )
    @Select("select * from `drive_config` where `parent_client`=#{aClient} and `parent_account`=#{aAccount} order by `default_target_flag` desc")
    LinkedList<DriveConfigDao> getByAccount(String aClient, String aAccount);

    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getDefaultByAccount(aClient, aAccount)",
                    imports = { "io.github.driveindex.admin.h2.service.DriveConfigService" }
            )
    )
    @Select("select * from `drive_config` where `parent_client`=#{aClient} and `parent_account`=#{aAccount} order by `default_target_flag` desc limit 1")
    DriveConfigDao getDefaultByAccount(String aClient, String aAccount);

    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getByConfig(aClient, aAccount, aConfig)",
                    imports = { "io.github.driveindex.admin.h2.service.DriveConfigService" }
            )
    )
    @Nullable
    @Select("select * from `drive_config` where `parent_client`=#{aClient} and `parent_account`=#{aAccount} and `id`=#{aConfig}")
    DriveConfigDao getByConfig(String aClient, String aAccount, String aConfig);
}
