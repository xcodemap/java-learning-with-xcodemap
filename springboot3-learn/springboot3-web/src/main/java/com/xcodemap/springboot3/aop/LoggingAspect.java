package com.xcodemap.springboot3.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // 定义切点表达式
/*
    @Pointcut("execution(* com.xcodemap.example.controller.HelloController.hello(..))")
*/
    public void taskExecution() {}

    private void beforeHello(ProceedingJoinPoint joinPoint) {

    }

    private void afterHello(Object result) {

    }

    // 环绕通知
    @Around("taskExecution()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        beforeHello(joinPoint);
        Object result = joinPoint.proceed(); // 执行目标方法
        afterHello(result);
        return result;
    }
}

