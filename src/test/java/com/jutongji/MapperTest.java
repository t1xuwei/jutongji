package com.jutongji;

import com.jutongji.repository.PermissionMapper;
import com.jutongji.repository.UserMapper;
import com.jutongji.repository.UserRoleMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/4/9 14:29
 */
public class MapperTest extends AbstractTests{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Test
    public void TestMost(){
        userMapper.selectAll();
        userRoleMapper.selectAll();
        permissionMapper.selectAll();

        System.out.println("hi");
    }



}
