package com.wangzhenghe.dao;

import com.wangzhenghe.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    //查询所有用户
    List<User> getUserList();

    //根据id查询用户
    List<User> getUser(int id);

    User getUserById(Map<String, Object> map);

    //插入一个用户
    int InsertUser(User user);

    int addUser(Map<String, Object> map);
    //修改用户
    int UpdateUser(User user);

    //删除用户
    int deleteUser(int id);
}
