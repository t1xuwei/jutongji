package com.jutongji.controller;

import com.jutongji.repository.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class DemoController {

    @Autowired
    UserMapper userMapper;

    @ApiOperation("欢迎controller")
    @GetMapping("/hello")
    public String hello(){
        System.out.println("进入Controller");
        return "HELLO,xu!";
    }

    @ApiOperation("需要管理员权限访问")
    @GetMapping("/adminHello")
    @RequiresRoles("admin")
    @RequiresPermissions("sayhi")
    public String adminHello(){
        return "admin hello!!!";
    }





}
