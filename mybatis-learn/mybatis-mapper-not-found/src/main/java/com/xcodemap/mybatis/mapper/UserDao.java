package com.xcodemap.mybatis.mapper;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyBatisConfig.class);
        try {
            UserMapper mapper = context.getBean(UserMapper.class);
            Map<String, Object> params = new HashMap<>();
            params.put("age", 101);
            List<User> users = mapper.findUser(params);
            System.out.printf("Size:%d %s\n", users.size(), users);
        } finally {
            context.close();
        }
    }
}
