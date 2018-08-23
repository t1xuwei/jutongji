package com.jutongji.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/7/18.
 */
@Data
@Entity
@Table(name = "t_user")
public class User implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="email")
    private String email;
    @Column(name="last_logintime")
    private Date lastLogintime;
    @Column(name="visits")
    private Integer visits;
    @Column(name="reg_time")
    private Date regTime;
    @Column(name="user_status")
    private String userStatus;


    @Transient
    private List<UserRole> roles;


    @Override
    public boolean equals(Object obj) {
        return this.getUserId().equals(((User)obj).getUserId());
    }


    public static final Integer USER_STATUS_NEW = Integer.valueOf(0);// 新注册

    public static final Integer USER_STATUS_ACTIVE = Integer.valueOf(1);// 已激活

    public static final Integer USER_STATUS_INACTIVE = Integer.valueOf(2);// 已禁用

    public static final String USER_STATUS_NEW_NAME = "新注册";

    public static final String USER_STATUS_ACTIVE_NAME = "已激活";

    public static final String USER_STATUS_INACTIVE_NAME = "已禁用";

    public static final Integer USER_PHONE_NOT_VALIDATE = Integer.valueOf(0);// 未验证手机

    public static final Integer USER_PHONE_VALIDATE = Integer.valueOf(1);// 已验证手机

    public static final String USER_PHONE_NOT_VALIDATE_STR = "未验证手机";

    public static final String USER_PHONE_VALIDATE_STR = "已验证手机";

    public static final Integer USER_EMAIL_NOT_VALIDATE = Integer.valueOf(0); // 未验证邮箱

    public static final Integer USER_EMAIL_VALIDATE = Integer.valueOf(1); // 已验证邮箱

    public static final String USER_EMAIL_VALIDATE_NAME = "未验证邮箱";

    public static final String USER_EMAIL_NOT_VALIDATE_NAME = "已验证邮箱";

    public static final Integer USER_TYPE_NORMAL = Integer.valueOf(1); // 普通用户

    public static final Integer USER_TYPE_COMPANY_OWNER = Integer.valueOf(2); // 企业主
}
