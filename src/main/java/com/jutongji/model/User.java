package com.jutongji.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
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
    @Transient
    private List<UserRole> roles;


    @Override
    public boolean equals(Object obj) {
        return this.getUserId().equals(((User)obj).getUserId());
    }
}
