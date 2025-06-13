package org.demo.permission.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PermissionDao {

    @Insert("INSERT INTO user_roles(user_id, role_id) VALUES (#{userId}, #{roleId})")
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Select("SELECT role_code FROM user_roles JOIN roles ON user_roles.role_id = roles.role_id WHERE user_id = #{userId}")
    String getRoleCodeByUserId(@Param("userId") Long userId);

    @Select("SELECT role_id FROM user_roles WHERE user_id = #{userId}")
    Long getRoleIdByUserId(@Param("userId") Long userId);

    List<Long> getUserIdsByStartUserId(@Param("startUserId") Long startUserId, @Param("limit") Integer limit, @Param("limitRole") Long limitRole);

    @Update("UPDATE user_roles SET role_id = #{roleId} WHERE user_id = #{userId}")
    void updateUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);


}
