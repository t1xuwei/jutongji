package com.jutongji.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "t_subject")
public class Subject {
    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Integer id;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "subject_desc")
    private String subjectDesc;

    @Column(name = "created_by")
    private String createdBy;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private Date updatedAt;


}