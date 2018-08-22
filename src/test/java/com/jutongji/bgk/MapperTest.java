package com.jutongji.bgk;

import com.jutongji.bgk.dao.UserMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/4/9 14:29
 */
public class MapperTest extends AbstractTests{

    @Autowired
    private UserMapper userMapper;
    @Test
    public void TestMost(){
        List a = userMapper.selectAll();
        System.out.println("hi");
    }



}
