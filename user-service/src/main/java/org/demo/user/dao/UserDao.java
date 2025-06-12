package org.demo.user.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.demo.common.domain.po.User;

@Mapper
public interface UserDao {



    @Insert("INSERT INTO users(username, password, email, phone, gmt_create) VALUES (#{username}, #{password}, #{email}, #{phone}, #{gmtCreate})")
    void insertUser(User user);

}
