package com.wangzhenghe.dao;

import com.wangzhenghe.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    //查询所有用户
    List<User> getUserList();

}
