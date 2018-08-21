package com.jutongji.bgk;


import com.jutongji.bgk.dao.TaskWorkMapper;
import com.jutongji.bgk.model.TaskWork;
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
    TaskWorkMapper taskWorkMapper;

    @Test
    public void TestSelectByOwnerIdAndOwnerTypeAndHomeworkIdAndUserIdAndTypeAndDeleted(){
       Integer id =  taskWorkMapper.selectByOwnerIdAndOwnerTypeAndHomeworkIdAndUserIdAndTypeAndDeleted(3,"t_training_course",2,5,"note",0);
       System.out.println(id);
    }

    @Test
    public void TestMost(){
        List<TaskWork> list = new ArrayList<>();
        for(Integer i=100 ;i<120 ;i++){
            TaskWork taskWork = new TaskWork();
            taskWork.setUserId(i);
            taskWork.setHomeworkId(i+1000);
            taskWork.setUserName("userName" + i);
            taskWork.setCreatedAt(new Date());
            list.add(taskWork);
        }
        taskWorkMapper.insertList(list);
    }



}
