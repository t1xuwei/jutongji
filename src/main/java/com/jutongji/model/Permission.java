package com.jutongji.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_permission")
public class Permission {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Integer id;
    @Column(name = "permission_name")
    private String permissionName;
    @Column(name="role_id")
    private Integer roleId;


}