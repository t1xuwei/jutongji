/**
 * @(#)IUserService.java
 *
 * @author zhangy
 * @version 1.0 2015-7-8
 *
 * Copyright (C) 2000,2015 , KOAL, Inc.
 */
package com.jutongji.service;

import com.jutongji.exception.ServiceException;
import com.jutongji.model.User;


/**
 * Purpose:
 * 
 * @see
 * @since 1.1.0
 */
public interface IUserService<T extends User>
{
    User login(String userName, String password) throws ServiceException;

    User insert(User user) throws ServiceException;

    User findUserByName(String userName) throws ServiceException;

    User updateUserLoginTime(User user) throws ServiceException;

    User digestUserName(User user) throws ServiceException;

    User updateUserPassword(User oldUser) throws ServiceException;

    User findUserByUserId(Integer userId) throws ServiceException;

    User update(User user);
}

/**
 * $Log: IUserService.java,v $
 * 
 * @version 1.0 2015-7-8
 */
