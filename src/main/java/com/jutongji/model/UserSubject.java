package com.jutongji.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_subject")
public class UserSubject {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Integer id;

    @Column(name ="user_id")
    private Integer userId;

    @Column(name = "subject_id")
    private Integer subjectId;


}