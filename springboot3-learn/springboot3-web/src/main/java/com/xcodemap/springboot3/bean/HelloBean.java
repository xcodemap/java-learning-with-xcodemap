package com.xcodemap.springboot3.bean;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloBean implements InitializingBean {

    @Value("${hello.name}")
    private String name;

    // 初始化方法是否调用的标志
    private boolean postConstructCalled;
    private boolean afterPropertiesSetCalled;


    public HelloBean() {
        System.out.println("1. new");
    }

    // 使用 @PostConstruct 执行初始化操作
    @PostConstruct
    private void doInit() {
        postConstructCalled = true;
        System.out.println("3. doInit() - @PostConstruct");
    }

    // 实现 InitializingBean 接口的 afterPropertiesSet 方法
    @Override
    public void afterPropertiesSet() throws Exception {
        afterPropertiesSetCalled = true;
        System.out.println("4. afterPropertiesSet() - InitializingBean");
    }



    public boolean isPostConstructCalled() {
        return postConstructCalled;
    }

    public boolean isAfterPropertiesSetCalled() {
        return afterPropertiesSetCalled;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        //System.out.println("2. set name");
        this.name = name;
    }

    public void setPostConstructCalled(boolean postConstructCalled) {
        this.postConstructCalled = postConstructCalled;
    }

    public void setAfterPropertiesSetCalled(boolean afterPropertiesSetCalled) {
        this.afterPropertiesSetCalled = afterPropertiesSetCalled;
    }
}

