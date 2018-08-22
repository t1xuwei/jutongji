package com.jutongji.bgk.controller;

import com.jutongji.bgk.dao.TaskWorkMapper;
import com.jutongji.bgk.model.TaskWork;
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
    TaskWorkMapper taskWorkMapper;

    @ApiOperation("欢迎controller")
    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("进入Controller");
        TaskWork taskWork = new TaskWork();
        taskWork.setUserId(222);
        taskWork.setHomeworkId(10);
        taskWork.setCreatedAt(new Date());
        taskWorkMapper.insert(taskWork);
        return "HELLO,xu!";
    }



}
