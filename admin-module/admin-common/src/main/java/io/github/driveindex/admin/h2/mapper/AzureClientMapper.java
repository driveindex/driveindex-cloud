package io.github.driveindex.admin.h2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.admin.h2.dao.AzureClientDao;
import kotlin.Deprecated;
import kotlin.ReplaceWith;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:07
 */
@Mapper
public interface AzureClientMapper extends BaseMapper<AzureClientDao> {
    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getAll()",
                    imports = { "io.github.driveindex.admin.h2.service.AzureClientService" }
            )
    )
    @Select("select * from `azure_client` order by `default_target_flag` desc")
    List<AzureClientDao> getAll();

    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getDefault()",
                    imports = { "io.github.driveindex.admin.h2.service.AzureClientService" }
            )
    )
    @Select("select * from `azure_client` order by `default_target_flag` desc limit 1")
    AzureClientDao getDefault();

    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getById(aClient)",
                    imports = { "io.github.driveindex.admin.h2.service.AzureClientService" }
            )
    )
    @Nullable
    @Select("select * from `azure_client` where `id`=#{aClient}")
    AzureClientDao getById(String aClient);
}
