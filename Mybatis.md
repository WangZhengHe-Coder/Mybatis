## 1、什么是MyBatis

**可能出现问题说明：Maven静态资源过滤问题**

```
<resources>
   <resource>
       <directory>src/main/java</directory>
       <includes>
           <include>**/*.properties</include>
           <include>**/*.xml</include>
       </includes>
       <filtering>false</filtering>
   </resource>
   <resource>
       <directory>src/main/resources</directory>
       <includes>
           <include>**/*.properties</include>
           <include>**/*.xml</include>
       </includes>
       <filtering>false</filtering>
   </resource>
</resources>
```

## 2、第一个Mybatis程序

思路：搭建环境--->导入Mybatis--->

### **2.1、搭建环境**：

#### **2.1.1、建立MySQL数据库**

```sql
CREATE DATABASE `mybatis`

USE mybatis;
//在MySQL数据库中数据库名或表名都是用 `` 给包起来的，而字符串使用 '' 英文单引号包围着
CREATE TABLE `user`(
  `id` INT(20) NOT NULL PRIMARY KEY,
  `name` VARCHAR(30) DEFAULT NULL,
  `pwd` VARCHAR(30) DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `user`(`id`,`name`,`pwd`) VALUES
(1,'张三','1234'),
(2,'李四','1111'),
(3,'王五','0000');
```

#### 2.1.2、新建项目

##### 	【1】新建maven项目

##### 	【2】删除src目录

**使Mybatis-study成为父工程**

![image-20210204202505748](C:\Users\h6410\AppData\Roaming\Typora\typora-user-images\image-20210204202505748.png)

##### 	【3】导入maven依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.wangzhenghe</groupId>
    <artifactId>Mybatis-study</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--导入依赖-->
    <dependencies>

        <!--导入数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.6</version>
        </dependency>

        <!--导入Mybatis-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>

        <!--导入Junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
    </dependencies>

</project>
```

### 	2.2、创建模块

1. **编写Mybatis的核心配置文件（做了一件事就是连接数据库）**

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   
   <!--核心配置文件-->
   <configuration>
   
       <!--环境配置-->
       <environments default="development">
           <environment id="development">
   
               <!--事务配置-->
               <transactionManager type="JDBC"/>
               <dataSource type="POOLED">
                   <property name="driver" value="com.mysql.jdbc.Driver"/>
                   <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                   <property name="username" value="root"/>
                   <property name="password" value="root"/>
               </dataSource>
           </environment>
       </environments>
       <mappers>
           <mapper resource="org/mybatis/example/BlogMapper.xml"/>
       </mappers>
   </configuration>
   ```

   

2. 编写Mybatis工具类

   ```java
   package com.wangzhenghe.utils;
   
   import org.apache.ibatis.io.Resources;
   import org.apache.ibatis.session.SqlSession;
   import org.apache.ibatis.session.SqlSessionFactory;
   import org.apache.ibatis.session.SqlSessionFactoryBuilder;
   
   import java.io.IOException;
   import java.io.InputStream;
   
   //获取SqlSessionFactory--->sqlSession
   public class MybatisUtils {
   
       private static SqlSessionFactory sqlSessionFactory;
       static {
           try {
               //使用Mybatis的第一步，获取SqlSessionFactory对象
               String resource = "mybatis-config.xml";
               InputStream inputStream = Resources.getResourceAsStream(resource);
               sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   
   
       //既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
       // SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。
   
       public static SqlSession getSqlSession(){
   //        SqlSession sqlSession = sqlSessionFactory.openSession();
           return sqlSessionFactory.openSession();
           //sqlSession类似于connection对象
   
       }
   }
   
   ```

### 2.3、编写代码

实体类

```java
package com.wangzhenghe.pojo;

public class User {

    private int id;
    private String name;
    private  String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}

```

DAO接口（Dao的专业名词是Mapper）

```java
package com.wangzhenghe.dao;

import com.wangzhenghe.pojo.User;

import java.util.List;

public interface UserDao {
    List<User> getUserList();
}

```



接口实现类由原来的iml转换为mapper配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.wangzhenghe.dao.UserDao">
    <select id="getUserList" resultType="com.wangzhenghe.pojo.User">
        select * from mybatis.user;
    </select>
</mapper>
```

### 2.4、测试

###### **MapperRegistry是什么？**

核心配置文件中注册mappers

**junit测试**

```java
package com.wangzhenghe;

import com.wangzhenghe.dao.UserDao;
import com.wangzhenghe.pojo.User;
import com.wangzhenghe.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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

```

可能会遇到的问题：

​		1、配置文件未注册

​		2、绑定接口错误

​		3、方法名错误

​		4、返回类型错误

​		5、maven导出资源（<build>）

### 思路：

​		![image-20210205191930952](C:\Users\h6410\AppData\Roaming\Typora\typora-user-images\image-20210205191930952.png)

## 3、CRUD

**只跟接口和配置文件有关，与实体类和工具类无关，所以无需修改**

修改内容：

**namespace**

namespace中的包名要和Dao/Mapper接口的包名一致！

**select查询标签**

​	id：就是对应的namspace中对应的方法名

​	parameterType：对应namespace中方法对应的参数类型

​	resultType：对应的是方法的返回值类型，如果是集合，那么返回值类型是集合的泛型

步骤：

建立UserMapper接口

编写抽象方法

```java
public interface UserMapper {

    //查询所有用户
    List<User> getUserList();

    //根据id查询用户
    List<User> getUser(int id);

    //插入一个用户
    int InsertUser(User user);

    //修改用户
    int UpdateUser(User user);

    //删除用户
    int deleteUser(int id);
}
```



建立UserMapper的xml文件

建立对应的sql标签实现sql语句

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.wangzhenghe.dao.UserMapper">
    <select id="getUserList" resultType="com.wangzhenghe.pojo.User">
        select * from mybatis.user;
    </select>
    <select id="getUser" parameterType="int" resultType="com.wangzhenghe.pojo.User">
        select * from mybatis.user WHERE id = #{id};
    </select>
    
    <insert id="InsertUser" parameterType="com.wangzhenghe.pojo.User">
        INSERT into mybatis.user (id, name, pwd) VALUES (#{id},#{name},#{pwd});
    </insert>

    <update id="UpdateUser" parameterType="com.wangzhenghe.pojo.User">
        UPDATE mybatis.user SET name = #{name},pwd = #{pwd} WHERE id = #{id};
    </update>
    
    <delete id="deleteUser" parameterType="">
        DELETE FROM mybatis.user WHERE id = #{id};
    </delete>
</mapper>int
```

测试

```java
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
}

```

## 4、分析错误

- 标签不要匹配错
- resource绑定mapper，需要使用路径（是"/"不是"."）
- 程序配置文件必须符合规范
- NullPointerException，没有注册到资源
- 输出的xml文件中存在中文乱码问题
- maven资源没有导出问题

## 5、万能的Map

为了解决修改部分列的值，有效解决方案

- UserMapper接口

  ```java
  int addUser(Map<String,Object> map);
  ```

  

- UserMapper.xml

  ```xml
  <insert id="addUser" parameterType="map">
      INSERT into user (id,name,pwd) values(#{id},#{name},#{pwd});
  </insert>
  ```

  

- 测试类

  ```java
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
  ```

## 6、配置解析

### 6.1、核心配置文件

![image-20210206202714587](C:\Users\h6410\AppData\Roaming\Typora\typora-user-images\image-20210206202714587.png)

- mybatis-configs.xml
- Mybatis的配置文件包含了会深深影响Mybatis行为的设置和属性信息

### 6.2、环境配置

Mybatis可以配置成适应多种环境

**不过要记住：尽管可以配置多个环境，但每个SqlSessionFactory实例只能选择一种环境**

学会使用配置多套环境！

```xml
<!--环境配置-->
    <environments default="test">
        <environment id="development">

            <!--事务配置 JDBC/MANAGED-->
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>

        <environment id="test">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
```



Mybatis默认的事务管理器就是JDBC，连接池：POOLED

### 6.3、属性（properties）

我们可以通过Properties属性来实现引用配置文件

这些属性都是可外部配置且可动态替换的，既可以在典型的Java属性文件中配置，亦可通过properties元素的子元素来传递。【db.properties】

**Properties属性来实现引用配置文件步骤：**

- 编写一个配置文件db.properties

  ```properties
  driver=com.mysql.jdbc.Driver
  url=jdbc:mysql://localhost:3306/mybatis?useSSL=false&useUnicode=true&characterEncoding=UTF-8
  username=root
  password=root
  ```

- 在核心配置文件中引入db.properties

  ```xml
  <properties resource="db.properties"/>
  ```

  - 可以直接引入外部文件
  - 可以在其中增加一些属性配置
  - 如果两个文件有同一字段，优先使用外部配置文件

- 在整个配置文件中用来替换需要动态配置的属性值

  ```xml
  <configuration>
      <!--引入外部配置文件-->
      <properties resource="db.properties"/>
      <!--环境配置-->
      <environments default="development">
          <environment id="development">
              <!--事务配置 JDBC/MANAGED-->
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
                  <property name="driver" value="${driver}"/>
                  <property name="url" value="${url}"/>
                  <property name="username" value="${username}"/>
                  <property name="password" value="${password}"/>
              </dataSource>
          </environment>
  
          <environment id="test">
              <transactionManager type="JDBC"/>
              <dataSource type="POOLED">
                  <property name="driver" value="com.mysql.jdbc.Driver"/>
                  <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                  <property name="username" value="root"/>
                  <property name="password" value="root"/>
              </dataSource>
          </environment>
      </environments>
      <mappers>
          <mapper resource="com/wangzhenghe/dao/UserMapper.xml"/>
      </mappers>
  
  
  </configuration>
  ```

### 6.4、类型别名（typeAliases）

- 类型别名是为Java类型设置一个短的名字。

- 存在的意义仅在于用来减少类完全限定名的冗余。

  ```xml
  <!--类型别名-->
      <typeAliases>
          <typeAlias type="com.wangzhenghe.pojo.User" alias="User"/>
      </typeAliases>
  ```

  也可以指定一个包名，Mybatis会在包名下面搜索需要的Java bean，比如：扫描实体类的包，它的默认别名就为这个类的类名首字母小写！

  ```xml
  <typeAliases>
      <package name="com.wangzhenghe.pojo"/>
  </typeAliases>
  ```

  在实体类比较少的时候使用第一种方式。

  如果实体类十分多，建议使用第二种。

  第一种可以DIY别名，第二种则不行，如果非要改。可以在实体上使用注解

  ```java
  @Alias("User")
  public class User {
  	······
  }
  ```

### 6.5、映射器（mappers）

MapperRegistry：注册绑定我们的Mapper文件

- **方式一：使用resource文件绑定注册【推荐使用】**

  ```xml
  <mappers>
      <mapper resource="com/wangzhenghe/dao/UserMapper.xml"/>
  </mappers>
  ```

- **方式二：使用class文件绑定注册**

  ```xml
  <mappers>
      <mapper class="com.wangzhenghe.dao.UserMapper"/>
  </mappers>
  ```

  注意点：

  - 接口和他的Mapper配置文件必须同名
  - 接口和他的Mapper配置文件必须在同一个包下

- **方式三：使用扫描包进行注入绑定**

  ```xml
  <mappers>
      <package name="com.wangzhenghe.dao"/>
  </mappers>
  ```

  注意点：

  - 接口和他的Mapper配置文件必须同名
  - 接口和他的Mapper配置文件必须在同一个包下

### 6.6、生命周期和作用域

生命周期和作用域是至关重要的，因为错误的使用会导致非常严重的并发问题。

**SqlSessionFactoryBuilder：**

- 一旦创建了SqlSessionFactory，就不再需要它了
- 局部变量

![image-20210207150354266](C:\Users\h6410\AppData\Roaming\Typora\typora-user-images\image-20210207150354266.png)

**SqlSessionFactory：**

- 说白了就是可以想象为：数据库连接池
- SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例
- SqlSessionFactory 的最佳作用域是应用作用域
- 最简单的就是使用单例模式或者静态单例模式

**SqlSession:**

- 连接池的一个请求！

- SqlSession的实例不是线程安全的，因此是不能被共享的，所以它的最佳作用域是请求或方法作用域

- 用完之后需要赶紧关闭，否则资源被占用

  ![image-20210207151754044](C:\Users\h6410\AppData\Roaming\Typora\typora-user-images\image-20210207151754044.png)

每一个Mapper代表了一个业务，也就是执行一个业务！

## 7、多对一处理

- 多个学生对一个老师
- 对学生而言，多个学生**关联**一个老师
- 对老师而言，一个老师有多个学生  **集合**

步骤：

- 创建student表和teacher表

  ```sql
  CREATE TABLE `teacher`(
     `id` INT(10) NOT NULL,
     `name` VARCHAR(30) DEFAULT NULL,
     PRIMARY KEY(`id`)
  )ENGINE=INNODB DEFAULT CHARSET=utf8;
  
  INSERT INTO teacher(`id`,`name`) VALUES(1,'王老师');
  
  CREATE TABLE `student`(
      `id` INT(10) NOT NULL,
      `name` VARCHAR(30) DEFAULT NULL,
      `tid` INT(10) DEFAULT NULL,
      PRIMARY KEY(`id`),
      KEY `fktid` (`tid`),
      CONSTRAINT `fktid` FOREIGN KEY(`tid`) REFERENCES `teacher`(`id`)
  )ENGINE=INNODB DEFAULT CHARSET=utf8;
  INSERT INTO student(`id`,`name`,`tid`) VALUES(0001,"张三","1");
  INSERT INTO student(`id`,`name`,`tid`) VALUES(0002,"李四","1");
  INSERT INTO student(`id`,`name`,`tid`) VALUES(0003,"王五","1");
  INSERT INTO student(`id`,`name`,`tid`) VALUES(0004,"赵华","1");
  ```

  多对一的理解：
  
  - 多个学生对应一个老师
  - 如果对于学生这边，就是一个多对一的现象，即从学生这边关联一个老师！
  
  > 数据库设计
  
  
  
  ![图片](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LPbib5To6slfFhMArq5QvCjofjccx37cuQgKsWEHibax0bDiaicU6ojNfEzWrj3TibFsX3MJju4sAp5Pg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
  
  ```sql
  CREATE TABLE `teacher` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
  ) ENGINE=INNODB DEFAULT CHARSET=utf8
  
  INSERT INTO teacher(`id`, `name`) VALUES (1, '秦老师');
  
  CREATE TABLE `student` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  `tid` INT(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fktid` (`tid`),
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
  ) ENGINE=INNODB DEFAULT CHARSET=utf8
  
  
  INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('1', '小明', '1');
  INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('2', '小红', '1');
  INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('3', '小张', '1');
  INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('4', '小李', '1');
  INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('5', '小王', '1');
  ```
  
  > 搭建测试环境
  
  1、IDEA安装Lombok插件
  
  2、引入Maven依赖
  
  ```xml
  <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
  <dependency>
   <groupId>org.projectlombok</groupId>
   <artifactId>lombok</artifactId>
   <version>1.16.10</version>
  </dependency>
  ```
  
  3、在代码中增加注解
  
  ```java
  @Data //GET,SET,ToString，有参，无参构造
  public class Teacher {
     private int id;
     private String name;
  }
  @Data
  public class Student {
     private int id;
     private String name;
     //多个学生可以是同一个老师，即多对一
     private Teacher teacher;
  }
  ```
  
  4、编写实体类对应的Mapper接口 【两个】
  
  - **无论有没有需求，都应该写上，以备后来之需！**
  
  ```java
  public interface StudentMapper {
  }
  public interface TeacherMapper {
  }
  ```
  
  5、编写Mapper接口对应的 mapper.xml配置文件 【两个】
  
  - **无论有没有需求，都应该写上，以备后来之需！**
  
  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.kuang.mapper.StudentMapper">
  
  </mapper>
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.kuang.mapper.TeacherMapper">
  
  </mapper>
  ```
  
  
  
  > 按查询嵌套处理
  
  1、给StudentMapper接口增加方法
  
  ```
  //获取所有学生及对应老师的信息
  public List<Student> getStudents();
  ```
  
  2、编写对应的Mapper文件
  
  ```
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.kuang.mapper.StudentMapper">
  
     <!--
     需求：获取所有学生及对应老师的信息
     思路：
         1. 获取所有学生的信息
         2. 根据获取的学生信息的老师ID->获取该老师的信息
         3. 思考问题，这样学生的结果集中应该包含老师，该如何处理呢，数据库中我们一般使用关联查询？
             1. 做一个结果集映射：StudentTeacher
             2. StudentTeacher结果集的类型为 Student
             3. 学生中老师的属性为teacher，对应数据库中为tid。
                多个 [1,...）学生关联一个老师=> 一对一，一对多
             4. 查看官网找到：association – 一个复杂类型的关联；使用它来处理关联查询
     -->
     <select id="getStudents" resultMap="StudentTeacher">
      select * from student
     </select>
     <resultMap id="StudentTeacher" type="Student">
         <!--association关联属性 property属性名 javaType属性类型 column在多的一方的表中的列名-->
         <association property="teacher"  column="tid"javaType="Teacher" select="getTeacher"/>
     </resultMap>
     <!--
     这里传递过来的id，只有一个属性的时候，下面可以写任何值
     association中column多参数配置：
         column="{key=value,key=value}"
         其实就是键值对的形式，key是传给下个sql的取值名称，value是片段一中sql查询的字段名。
     -->
     <select id="getTeacher" resultType="teacher">
        select * from teacher where id = #{id}
     </select>
  
  </mapper>
  ```
  
  3、编写完毕去Mybatis配置文件中，注册Mapper！
  
  4、注意点说明：
  
  ```
  <resultMap id="StudentTeacher" type="Student">
     <!--association关联属性 property属性名 javaType属性类型 column在多的一方的表中的列名-->
     <association property="teacher"  column="{id=tid,name=tid}"javaType="Teacher" select="getTeacher"/>
  </resultMap>
  <!--
  这里传递过来的id，只有一个属性的时候，下面可以写任何值
  association中column多参数配置：
     column="{key=value,key=value}"
     其实就是键值对的形式，key是传给下个sql的取值名称，value是片段一中sql查询的字段名。
  -->
  <select id="getTeacher" resultType="teacher">
    select * from teacher where id = #{id} and name = #{name}
  </select>
  ```
  
  5、测试
  
  ```
  @Test
  public void testGetStudents(){
     SqlSession session = MybatisUtils.getSession();
     StudentMapper mapper = session.getMapper(StudentMapper.class);
  
     List<Student> students = mapper.getStudents();
  
     for (Student student : students){
         System.out.println(
                 "学生名:"+ student.getName()
                         +"\t老师:"+student.getTeacher().getName());
    }
  }
  ```
  
  
  
  > 按结果嵌套处理
  
  除了上面这种方式，还有其他思路吗？
  
  我们还可以按照结果进行嵌套处理；
  
  1、接口方法编写
  
  ```
  public List<Student> getStudents2();
  ```
  
  2、编写对应的mapper文件
  
  ```xml
  <!--
  按查询结果嵌套处理
  思路：
     1. 直接查询出结果，进行结果集的映射
  -->
  <select id="getStudents2" resultMap="StudentTeacher2" >
    select s.id sid, s.name sname , t.name tname
    from student s,teacher t
    where s.tid = t.id
  </select>
  
  <resultMap id="StudentTeacher2" type="Student">
     <id property="id" column="sid"/>
     <result property="name" column="sname"/>
     <!--关联对象property 关联对象在Student实体类中的属性-->
     <association property="teacher" javaType="Teacher">
         <result property="name" column="tname"/>
     </association>
  </resultMap>
  ```
  
  3、去mybatis-config文件中注入【此处应该处理过了】
  
  4、测试
  
  ```
  @Test
  public void testGetStudents2(){
     SqlSession session = MybatisUtils.getSession();
     StudentMapper mapper = session.getMapper(StudentMapper.class);
  
     List<Student> students = mapper.getStudents2();
  
     for (Student student : students){
         System.out.println(
                 "学生名:"+ student.getName()
                         +"\t老师:"+student.getTeacher().getName());
    }
  }
  ```
  
  > 小结
  
  按照查询进行嵌套处理就像SQL中的子查询
  
  按照结果进行嵌套处理就像SQL中的联表查询
  
  
  
  
  
  
  
  一对多处理
  
  ## 一对多的处理
  
  一对多的理解：
  
  - 一个老师拥有多个学生
  - 如果对于老师这边，就是一个一对多的现象，即从一个老师下面拥有一群学生（集合）！
  
  > 实体类编写
  
  ```
  @Data
  public class Student {
     private int id;
     private String name;
     private int tid;
  }
  @Data 
  public class Teacher {
     private int id;
     private String name;
     //一个老师多个学生
     private List<Student> students;
  }
  ```
  
  ..... 和之前一样，搭建测试的环境！
  
  
  
  > 按结果嵌套处理
  
  1、TeacherMapper接口编写方法
  
  ```
  //获取指定老师，及老师下的所有学生
  public Teacher getTeacher(int id);
  ```
  
  2、编写接口对应的Mapper配置文件
  
  ```
  <mapper namespace="com.kuang.mapper.TeacherMapper">
  
     <!--
     思路:
         1. 从学生表和老师表中查出学生id，学生姓名，老师姓名
         2. 对查询出来的操作做结果集映射
             1. 集合的话，使用collection！
                 JavaType和ofType都是用来指定对象类型的
                 JavaType是用来指定pojo中属性的类型
                 ofType指定的是映射到list集合属性中pojo的类型。
     -->
     <select id="getTeacher" resultMap="TeacherStudent">
        select s.id sid, s.name sname , t.name tname, t.id tid
        from student s,teacher t
        where s.tid = t.id and t.id=#{id}
     </select>
  
     <resultMap id="TeacherStudent" type="Teacher">
         <result  property="name" column="tname"/>
         <collection property="students" ofType="Student">
             <result property="id" column="sid" />
             <result property="name" column="sname" />
             <result property="tid" column="tid" />
         </collection>
     </resultMap>
  </mapper>
  ```
  
  3、将Mapper文件注册到MyBatis-config文件中
  
  ```
  <mappers>
     <mapper resource="mapper/TeacherMapper.xml"/>
  </mappers>
  ```
  
  4、测试
  
  ```
  @Test
  public void testGetTeacher(){
     SqlSession session = MybatisUtils.getSession();
     TeacherMapper mapper = session.getMapper(TeacherMapper.class);
     Teacher teacher = mapper.getTeacher(1);
     System.out.println(teacher.getName());
     System.out.println(teacher.getStudents());
  }
  ```
  
  
  
  > 按查询嵌套处理
  
  1、TeacherMapper接口编写方法
  
  ```
  public Teacher getTeacher2(int id);
  ```
  
  2、编写接口对应的Mapper配置文件
  
  ```
  <select id="getTeacher2" resultMap="TeacherStudent2">
  select * from teacher where id = #{id}
  </select>
  <resultMap id="TeacherStudent2" type="Teacher">
     <!--column是一对多的外键 , 写的是一的主键的列名-->
     <collection property="students" javaType="ArrayList"ofType="Student" column="id" select="getStudentByTeacherId"/>
  </resultMap>
  <select id="getStudentByTeacherId" resultType="Student">
    select * from student where tid = #{id}
  </select>
  ```
  
  3、将Mapper文件注册到MyBatis-config文件中
  
  4、测试
  
  ```
  @Test
  public void testGetTeacher2(){
     SqlSession session = MybatisUtils.getSession();
     TeacherMapper mapper = session.getMapper(TeacherMapper.class);
     Teacher teacher = mapper.getTeacher2(1);
     System.out.println(teacher.getName());
     System.out.println(teacher.getStudents());
  }
  ```
  
  > 小结
  
  1、关联-association
  
  2、集合-collection
  
  3、所以association是用于一对一和多对一，而collection是用于一对多的关系
  
  4、JavaType和ofType都是用来指定对象类型的
  
  - JavaType是用来指定pojo中属性的类型
  - ofType指定的是映射到list集合属性中pojo的类型。
  
  **注意说明：**
  
  1、保证SQL的可读性，尽量通俗易懂
  
  2、根据实际要求，尽量编写性能更高的SQL语句
  
  3、注意属性名和字段不一致的问题
  
  4、注意一对多和多对一 中：字段和属性对应的问题
  
  5、尽量使用Log4j，通过日志来查看自己的错误
  
  
  
  一对多和多对一对于很多人来说是难点，一定要大量的做练习理解！