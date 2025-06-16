package org.demo.user.dao;

import org.apache.ibatis.annotations.*;
import org.demo.common.domain.po.User;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("SELECT password FROM users WHERE user_id = #{userId}")
    String findPassword(@Param("userId") Long userId);

    @Select("SELECT user_id, username, email, phone, gmt_create FROM users WHERE user_id = #{userId}")
    User getUser(@Param("userId") Long userId);

    @Select("SELECT user_id, username, password FROM users WHERE username = #{username}")
    User getUserByUsername(@Param("username") String username);

    List<User> getUsers(@Param("userIds") List<Long> userIds);

    @Insert("INSERT INTO users(username, password, email, phone, gmt_create) VALUES (#{username}, #{password}, #{email}, #{phone}, #{gmtCreate})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUser(User user);

    @Update("UPDATE users SET email = #{user.email}, phone = #{user.phone} WHERE user_id = #{userId}")
    boolean updateUser(@Param("user") User user, @Param("userId") Long userId);

    @Update("UPDATE users SET password = #{password} WHERE user_id = #{userId}")
    boolean updatePassword(@Param("password") String password, @Param("userId") Long userId);


}
