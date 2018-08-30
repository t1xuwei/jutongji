package com.jutongji.service.impl;

import com.jutongji.model.DailyDemo;
import com.jutongji.repository.DailyDemoMapper;
import com.jutongji.service.IDailyDemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuw on 2017/8/9.
 */
@Service
public class DailyDemoService implements IDailyDemoService {
    @Resource
    private DailyDemoMapper dailyDemoMapper;


    public List<DailyDemo> searchAll() {
        return dailyDemoMapper.searchAll();
    }
}
