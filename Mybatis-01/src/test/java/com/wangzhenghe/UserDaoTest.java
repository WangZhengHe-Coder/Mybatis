package com.wangzhenghe;

import com.wangzhenghe.dao.UserDao;
import com.wangzhenghe.pojo.User;
import com.wangzhenghe.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {

    @Test
    public void test(){
        //第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //执行SQL
            //为了拿到SQL，要么从UserMapper中拿，要么从UserDao中拿，
            // 其实是从哪个里面拿都行，因为UserMapper是UserDao的实现，我们
            // 是面向接口编程，所以从UserDao中拿，通过UserDao.class利用反射技术获取SQL
        UserDao mapper = sqlSession.getMapper(UserDao.class);

        List<User> userList = mapper.getUserList();

        for (User user : userList) {
            System.out.println(user);
        }

        //关闭SqlSession
        sqlSession.close();

    }
}
