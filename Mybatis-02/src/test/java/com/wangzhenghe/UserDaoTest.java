package com.wangzhenghe;


import com.wangzhenghe.dao.UserMapper;
import com.wangzhenghe.pojo.User;
import com.wangzhenghe.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoTest {

    @Test
    public void test(){
        //第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //执行SQL
        //为了拿到SQL，要么从UserMapper中拿，要么从UserDao中拿，
        // 其实是从哪个里面拿都行，因为UserMapper是UserDao的实现，我们
        // 是面向接口编程，所以从UserDao中拿，通过UserDao.class利用反射技术获取SQL
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        List<User> userList = mapper.getUserList();

        for (User user : userList) {
            System.out.println(user);
        }

        //关闭SqlSession
        sqlSession.close();

    }

    @Test
    public void getUsertest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> user = mapper.getUser(1);

        for (User user1 : user) {
            System.out.println(user1);
        }

        sqlSession.close();
    }

    @Test
    public void getUserByIdtest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id",1);

        User user = mapper.getUserById(map);

        System.out.println(user);

        sqlSession.close();
    }

    @Test
    public void InsertTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int res = mapper.InsertUser(new User(4,"小明","2345"));


        sqlSession.commit();
        if (res>0){
            System.out.println("插入成功！");
        }
        //增删改需要提交事务

        sqlSession.close();
    }

    @Test
    public void UpdateTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int res = mapper.UpdateUser(new User(4,"张华","2345211"));


        sqlSession.commit();
        if (res>0){
            System.out.println("修改成功！");
        }
        //增删改需要提交事务

        sqlSession.close();
    }

    @Test
    public void DeleteUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int res = mapper.deleteUser(4);


        sqlSession.commit();
        if (res>0){
            System.out.println("删除成功！");
        }

        sqlSession.close();
    }


    @Test
    public void addUserTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id",5);
        map.put("name","李红");
        map.put("pwd","4334");
        int i = mapper.addUser(map);

        sqlSession.commit();
        if(i>0){
            System.out.println("插入成功！");
        }
        sqlSession.close();
    }
}
