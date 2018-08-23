/**
 * @(#)UserReg.java
 *
 * @author huawei
 * @version 1.0 2015-4-20
 *
 * Copyright (C) 2012,2015 , PING' AN, Inc.
 */
package com.jutongji.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Purpose:
 * 
 * @see
 * @since 1.1.0
 */
public class UserLogin
{
    String name;

    String password;

    String code;

    private String from;

    private Integer autoLogin;

    public Integer getAutoLogin()
    {
        return autoLogin;
    }

    public void setAutoLogin(Integer autoLogin)
    {
        this.autoLogin = autoLogin;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

}

/**
 * $Log: UserReg.java,v $
 * 
 * @version 1.0 2015-4-20
 */
