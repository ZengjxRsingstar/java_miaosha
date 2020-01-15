package com.zengjx.miaosha.dao;

import com.zengjx.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/13  19:33
 * @Version V1.0
 */
@Mapper
public interface UserDao {
    @Select("SELECT  id,name  FROM   USER   WHERE    id =#{id}")
    public User    getUserById( @Param("id") Integer  id);

    @Insert("insert into user (id, name) values  (#{id} ,#{name} ")
    public  int  inseartUser(User  user);
 }
