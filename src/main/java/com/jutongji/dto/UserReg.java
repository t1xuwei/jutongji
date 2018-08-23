/**
 * @(#)UserReg.java
 *
 * @author huawei
 * @version 1.0 2015-4-20
 *
 * Copyright (C) 2012,2015 , PING' AN, Inc.
 */
package com.jutongji.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Purpose:
 * 
 * @see
 * @since 1.1.0
 */
public class UserReg
{
    @NotBlank
    String username;

    @NotNull
    Integer count;

    @NotBlank(message = "密码不能为空")
    String password;

    String phoneCode;
    
    @NotBlank(message = "验证码不能为空")
    String code;

    private String from;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    
    public String getPhoneCode()
    {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode)
    {
        this.phoneCode = phoneCode;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

}

/**
 * $Log: UserReg.java,v $
 * 
 * @version 1.0 2015-4-20
 */
