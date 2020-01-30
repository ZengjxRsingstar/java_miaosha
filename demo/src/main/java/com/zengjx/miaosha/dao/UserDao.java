package com.zengjx.miaosha.dao;

import com.zengjx.miaosha.domain.User;
import org.apache.ibatis.annotations.*;

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

    @Insert("insert into user (name) values  (#{name} )")
    @Options(useGeneratedKeys = true,keyProperty ="id",keyColumn = "id")

    public  int  inseartUser(User  user);
 }
