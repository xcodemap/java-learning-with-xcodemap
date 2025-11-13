package com.xcodemap.springboot3.bean;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

public class WorldBean implements InitializingBean, DisposableBean {

    @Value("${hello.name}")
    private String name;

    // 初始化方法是否调用的标志
    private boolean postConstructCalled;
    private boolean afterPropertiesSetCalled;

    // 销毁方法是否调用的标志
    private boolean preDestroyCalled;
    private boolean destroyCalled;

    public WorldBean() {
        System.out.println("1. new world");
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

    // 使用 @PreDestroy 执行销毁操作
    @PreDestroy
    private void preDestroy() {
        preDestroyCalled = true;
        System.out.println("5. preDestroy() - @PreDestroy");
    }

    // 实现 DisposableBean 接口的 destroy 方法
    @Override
    public void destroy() throws Exception {
        destroyCalled = true;
        System.out.println("6. destroy() - DisposableBean");
    }



    public boolean isPostConstructCalled() {
        return postConstructCalled;
    }

    public boolean isAfterPropertiesSetCalled() {
        return afterPropertiesSetCalled;
    }


    public boolean isPreDestroyCalled() {
        return preDestroyCalled;
    }

    public boolean isDestroyCalled() {
        return destroyCalled;
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

    public void setPreDestroyCalled(boolean preDestroyCalled) {
        this.preDestroyCalled = preDestroyCalled;
    }

    public void setDestroyCalled(boolean destroyCalled) {
        this.destroyCalled = destroyCalled;
    }
}

