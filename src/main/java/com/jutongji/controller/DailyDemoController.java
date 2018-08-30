package com.jutongji.controller;

import com.jutongji.model.DailyDemo;
import com.jutongji.service.IDailyDemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Created by xuw on 2017/8/9.
 */
@Controller
public class DailyDemoController {

    @Resource
    private IDailyDemoService dailyDemoService;

    @RequestMapping("/demo")
    public String demoList(HttpServletRequest request){
        List<DailyDemo> dailyDemos = dailyDemoService.searchAll();
        Collections.shuffle(dailyDemos);
        request.setAttribute("demos", dailyDemos);
        request.setAttribute("demoCount",dailyDemos.size());
        return "demo";
    }
}
