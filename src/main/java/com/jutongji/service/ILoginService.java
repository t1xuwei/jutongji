package com.jutongji.service;

import com.jutongji.model.User;
import com.jutongji.model.UserRole;

import java.util.Map;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/8/22 17:00
 */
public interface ILoginService {
    //添加用户
    User addUser(Map<String, Object> map);

    //添加角色
    UserRole addRole(Map<String, Object> map);

    //查询用户通过用户名
    User findByName(String name);
}
