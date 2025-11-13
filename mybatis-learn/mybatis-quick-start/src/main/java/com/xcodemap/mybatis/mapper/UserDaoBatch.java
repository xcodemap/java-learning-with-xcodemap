package com.xcodemap.mybatis.mapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoBatch {

    public static void test(int i) {
        i++;
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        String resource = "mappers/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        for (int i = 0; i < 30; i++) {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                Map<String, Object> params = new HashMap<>();
                params.put("age", 100 + i);
                params.put("height", 120 + i);
                List<User> users = mapper.findUser(params);
                System.out.printf("Size:%d %s\n", users.size(), users);
                test(i);
            }
        }
        System.out.println("Cost " + (System.currentTimeMillis() - start));
    }
}
