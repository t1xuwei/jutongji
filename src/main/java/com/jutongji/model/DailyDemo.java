package com.jutongji.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by xuw on 2017/8/9.
 */
@Data
@Entity
@Table(name = "tb_daily_demo")
public class DailyDemo {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "desc")
    private String desc;


}
