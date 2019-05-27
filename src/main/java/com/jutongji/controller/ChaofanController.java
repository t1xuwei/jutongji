package com.jutongji.controller;

import com.jutongji.model.DailyDemo;
import com.jutongji.service.IDailyDemoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Created by xuw on 2017/8/9.
 */
@Controller
public class ChaofanController {

    @Resource
    private IDailyDemoService dailyDemoService;

    @RequestMapping("/chaofan")
    public String demoList(Model model){
        model.addAttribute("chaofan","");
        return "chaofan";
    }
}
