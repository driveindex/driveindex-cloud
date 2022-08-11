package io.github.driveindex.admin.h2.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.driveindex.admin.h2.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

/**
 * 数据表 user 映射
 * @author sgpublic
 * @Date 2022/8/5 17:34
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {
    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return 用户，若无此用户返回 null
     */
    @Nullable
    @Select("select * from `user` where `username`=#{username}")
    User findUserByName(String username);

    /**
     * 通过用户名获取用户的密码
     * @param username 用户名
     * @return 用户，若无此用户返回 null
     */
    @Nullable
    @Select("select `password` from `user` where `username`=#{username}")
    String getUserPassword(String username);
}
