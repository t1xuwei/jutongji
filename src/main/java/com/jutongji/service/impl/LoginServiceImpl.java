package com.jutongji.service.impl;

import com.jutongji.dao.PermissionMapper;
import com.jutongji.dao.UserMapper;
import com.jutongji.dao.UserRoleMapper;
import com.jutongji.model.Permission;
import com.jutongji.model.User;
import com.jutongji.model.UserRole;
import com.jutongji.service.ILoginService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    //添加用户
    @Override
    public User addUser(Map<String, Object> map) {
        User user = new User();
        user.setUsername(map.get("username").toString());
        user.setPassword((map.get("password").toString()));
        userMapper.insertUseGeneratedKeys(user);
        return user;
    }

    //添加角色
    @Override
    public UserRole addRole(Map<String, Object> map) {
        User user = userMapper.selectByPrimaryKey(Long.valueOf(map.get("userId").toString()));
        UserRole role = new UserRole();
        role.setRoleName(map.get("roleName").toString());
        role.setUserId(user.getUserId());
        roleMapper.insertUseGeneratedKeys(role);

        Permission permission1 = new Permission();
        permission1.setPermissionName("create");
        permission1.setRoleId(role.getId());

        Permission permission2 = new Permission();
        permission2.setPermissionName("update");
        permission2.setRoleId(role.getId());

        List<Permission> permissions = new ArrayList<Permission>();
        permissions.add(permission1);
        permissions.add(permission2);
        permissionMapper.insertList(permissions);
        return role;
    }

    //查询用户通过用户名
    @Override
    public User findByName(String name) {
        User userParam = new User();
        userParam.setUsername(name);
        List<User> users = userMapper.select(userParam);
        if(CollectionUtils.isEmpty(users))
            return null;
        User  user = users.get(0);
        UserRole roleParam = new UserRole();
        List<UserRole> roleList =roleMapper.select(roleParam);
        roleList.forEach(x -> {
            Permission permissionParam = new Permission();
            x.setPermissionList(permissionMapper.select(permissionParam));
        });
        user.setRoles(roleList);

        return user;
    }
}