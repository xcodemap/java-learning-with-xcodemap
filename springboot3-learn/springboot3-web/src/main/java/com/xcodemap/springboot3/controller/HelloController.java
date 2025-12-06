package com.xcodemap.springboot3.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class HelloController {

    Logger logger = LoggerFactory.getLogger(getClass());

    ExecutorService executor = Executors.newFixedThreadPool(1);

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return helloService.hello();
    }

    @GetMapping("/recordRequest")
    public String recordRequest(HttpServletRequest request) {
        Runnable runnable = () -> {
            String path = null;
            logger.info("request:{}", System.identityHashCode(request));
            try {
                Thread.sleep(200);
                path = request.getServletPath();
            } catch (Throwable ignored) {

            }
            logger.info("path:{}", path);
        };
        executor.execute(runnable);
        return "OK";
    }
}
