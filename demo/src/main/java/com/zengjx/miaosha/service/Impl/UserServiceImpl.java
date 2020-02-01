package com.zengjx.miaosha.service.Impl;

import com.zengjx.miaosha.dao.UserDao;
import com.zengjx.miaosha.domain.User;
import com.zengjx.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author zengjx
 * @Company zengjx
 * @Date 2020/1/13  19:58
 * @Version V1.0
 */
@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer id) {

        if(id<=0){

          //错误处理
        }

        return userDao.getUserById(id);
    }

    @Override
    public int inseartUser(User user) {

        int ret= userDao.inseartUser(user);
        System.out.println("ret ="+ret+"  userid"+user.getId());
        return ret;
    }



}
