package com.useless.core.discard.dao;

import com.useless.core.discard.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("SELECT * FROM user")
    List<User> findAll();
    @Select("SELECT * FROM user WHERE account = #{account}")
    User findByAccount(String account);
    @Insert("INSERT INTO user (account,password,gender,birth,interest,remark)"
            + "VALUES (#{user.account},#{user.password},#{user.gender},#{user.birth},#{user.interest},#{user.remark})")
    void insert(@Param("user") User user);
}
