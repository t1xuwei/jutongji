package com.jutongji.repository;

import com.jutongji.custommapper.CustomMapper;
import com.jutongji.model.DailyDemo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by xuw on 2017/8/9.
 */
@Mapper
public interface DailyDemoMapper extends CustomMapper<DailyDemo> {

    @Select("select * from tb_daily_demo")
    public List<DailyDemo> searchAll();


}
