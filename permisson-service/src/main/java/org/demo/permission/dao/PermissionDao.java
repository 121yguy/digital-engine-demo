package org.demo.permission.dao;

import org.apache.ibatis.annotations.*;

@Mapper
public interface PermissionDao {

    @Insert("INSERT INTO user_roles(user_id, role_id) VALUES (#{userId}, #{roleId})")
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Select("SELECT role_id FROM user_roles WHERE user_id = #{userId}")
    Long getRoleIdByUserId(@Param("userId") Long userId);

    @Update("UPDATE user_roles SET role_id = #{roleId} WHERE user_id = #{userId}")
    void updateUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);


}
