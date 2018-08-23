/**
 * @(#)LoginServiceImpl.java
 *
 * @author huawei
 * @version 1.0 2015-4-14
 *
 * Copyright (C) 2012,2015 , PING' AN, Inc.
 */
package com.jutongji.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.jutongji.repository.UserRepository;
import com.jutongji.exception.ErrorCode;
import com.jutongji.exception.ServiceException;
import com.jutongji.model.User;
import com.jutongji.service.IUserService;
import com.jutongji.util.SecureUtil;
import com.jutongji.util.security.DigestUtils;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;


/**
 * Purpose:
 * 
 * @see
 * @since 1.1.0
 */
@Service("userService")
public class UserServiceImpl implements IUserService<User>
{
    @Resource
    private UserRepository userRepository;

    @Override
    public User insert(User user) throws ServiceException
    {
        try
        {
            if (null != user.getEmail())
            {
                User userTemp = userRepository.findByEmail(user.getEmail());
                if (null != userTemp)
                {
                    user = userTemp;
                }
            }
            // 密码MD5加密
            // String originalStr = user.getEmail() + user.getPassword();
            String originalStr = user.getUsername() + user.getPassword();
            String password = SecureUtil.md5(SecureUtil.md5(originalStr));
            user.setPassword(password);
            user.setLastLogintime(new Timestamp(new Date().getTime()));
            user.setVisits(1);
            user.setRegTime(new Date());

            return userRepository.save(user);
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.DB_ERROR, "创建新用户失败" + e.getMessage(), e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.DB_ERROR, "创建新用户失败" + e.getMessage(), e);

        }
    }

    @Override
    public User login(String userName,String password) throws ServiceException
    {
        try
        {
            User record = findUserByName(userName);
            if (record == null)
            {
                throw new ServiceException(ErrorCode.ILLEGAL_REQUEST, "找不到对应的用户");
            }

            String passWordRecord = record.getPassword();

            // 重新计算摘要
            String originalStr = record.getUsername() + password;
            String hash = DigestUtils.md5(DigestUtils.md5(originalStr.getBytes("UTF-8")).getBytes("UTF-8"));
            if (!hash.equals(passWordRecord))
            {
                return null;
            }
            return record;
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.DB_ERROR, "查询用户密码信息失败" + e.getMessage(), e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.DB_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public User findUserByName(String userName) throws ServiceException
    {
        try
        {
            User object = userRepository.findByEmail(userName);
            return (object == null ? null : (User) object);
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.DB_ERROR, "根据用户名[" + userName + "]查询用户信息失败" + e.getMessage(), e);
        }
    }


    @Override
    public User updateUserLoginTime(User user) throws ServiceException
    {
        try
        {

            user.setLastLogintime(new Date());
            user.setVisits(user.getVisits() + 1);
            return userRepository.save(user);
        }
        catch (HibernateException e)
        {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.DB_ERROR, "更新用户登录信息失败" + e.getMessage(), e);
        }
    }

    @Override
    public User digestUserName(User user) throws ServiceException
    {
        return user;
    }

    // 判断是否电话号码
    private boolean isPhoneNumber(String input) throws ServiceException
    {
        String regex = "^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$";
        Pattern p = Pattern.compile(regex);
        return p.matcher(input).matches();
    }





}

/**
 * $Log: LoginServiceImpl.java,v $
 * 
 * @version 1.0 2015-4-14
 */
