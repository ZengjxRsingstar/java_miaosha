package com.zengjx.miaosha.service;

import com.zengjx.miaosha.dao.UserDao;
import com.zengjx.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/13  19:57
 * @Version V1.0
 */
public interface UserService {

  public User    getUserById(Integer  id);

  public   int inseartUser(User user);
}
