package com.xcodemap.springboot3.controller;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HelloService {
    public Map<String, String> hello() {
        HashMap hashMap = new HashMap<>();
        hashMap.put("hello", "xcodemap!");
        return hashMap;
    }
}
