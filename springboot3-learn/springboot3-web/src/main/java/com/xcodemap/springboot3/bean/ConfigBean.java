package com.xcodemap.springboot3.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {

    @Bean
    public WorldBean creatWorldBean() {
        return new WorldBean();
    }
}
