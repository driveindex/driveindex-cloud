package io.github.driveindex.admin.h2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.admin.h2.dao.AccountTokenDto;
import kotlin.Deprecated;
import kotlin.ReplaceWith;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:07
 */
@Mapper
public interface AccountTokenMapper extends BaseMapper<AccountTokenDto> {
    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getByClientId(aClient)",
                    imports = { "io.github.driveindex.admin.h2.service.AccountTokenService" }
            )
    )
    @Select("select * from `account_token` where `parent_client`=#{clientId} order by `default_target_flag` desc")
    LinkedList<AccountTokenDto> getByClientId(String aClient);

    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getDefaultByClientId(aClient)",
                    imports = { "io.github.driveindex.admin.h2.service.AccountTokenService" }
            )
    )
    @Select("select * from `account_token` where `parent_client`=#{clientId} order by `default_target_flag` desc limit 1")
    AccountTokenDto getDefaultByClientId(String aClient);

    @java.lang.Deprecated
    @Deprecated(
            message = "Use Service instead.",
            replaceWith = @ReplaceWith(
                    expression = "getByAccount(aClient, aAccount)",
                    imports = { "io.github.driveindex.admin.h2.service.AccountTokenService" }
            )
    )
    @Select("select * from `account_token` where `parent_client`=#{clientId} and `id`=#{accountId}")
    AccountTokenDto getByAccount(String aClient, String aAccount);
}