package com.jutongji.bgk.controller;

import com.jutongji.bgk.dao.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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



}
