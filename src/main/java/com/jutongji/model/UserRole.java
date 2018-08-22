package com.jutongji.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "t_user_role")
public class UserRole {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Integer id;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "user_id")
    private Integer userId;

    @Transient
    private List<Permission> permissionList;

}