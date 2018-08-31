package com.jutongji.controller;

import com.jutongji.util.Data;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/8/31 14:24
 */
@Controller
public class SubjectController {

    @ApiOperation("添加主题")
    @PostMapping("/subject/add")
    public Data<?> addSubject(){
        return Data.success();
    }

    @ApiOperation("删除主题")
    @GetMapping("/subject/del")
    public Data<?> delSubject(){
        return Data.success();
    }

    @ApiOperation("查看主题记录")
    @PostMapping("/subject/list")
    public Data<?> subjectList(){
        return Data.success();
    }

    @ApiOperation("添加主题的记录")
    @PostMapping("/record/add")
    public Data<?> addSubjectRecord(){
        return Data.success();
    }

    @ApiOperation("删除记录")
    @GetMapping("/record/del")
    public Data<?> delRecord(){
        return Data.success();
    }

    @ApiOperation("根据时间查询记录")
    @GetMapping("/record/list")
    public Data<?> recordList(){
        return Data.success();
    }


}
