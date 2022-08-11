package io.github.driveindex.admin.h2.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.admin.h2.dao.AccountTokenDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/7 17:07
 */
@Mapper
public interface AccountTokenRepository extends BaseMapper<AccountTokenDao> {
    @Select("select * from `account_token` where `parent_client`=#{clientId} order by `default_target_flag` desc")
    LinkedList<AccountTokenDao> getByClientId(String clientId);

    @Select("select * from `account_token` where `parent_client`=#{clientId} and `id`=#{accountId}")
    AccountTokenDao getByAccount(String clientId, String accountId);
}
