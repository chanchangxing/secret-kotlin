package com.example.project.mapper

import com.example.project.bean.UserBean
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Insert("insert into user (name, password) values(#{name}, #{password})")
    fun register(@Param("name") name: String,
                 @Param("password") password: String): Int

    @Select("select id, name from user where id = (select last_insert_id())")
    fun getUserInfoById(@Param("id") id: Int): UserBean
}