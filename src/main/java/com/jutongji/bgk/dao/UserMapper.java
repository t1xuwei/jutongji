package com.jutongji.bgk.dao;

import com.jutongji.bgk.custommapper.CustomMapper;
import com.jutongji.bgk.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by wei on 2017/7/18.
 */
@Mapper
public interface UserMapper extends CustomMapper<User>{

    @Select("select * from tb_user where username=#{username}")
    public User findByName(String username);


    @Update("update tb_user set password=#{password} where username =#{username} ")
    public void changePwdByPhone(@Param(value = "username") String phone, @Param(value = "password") String password);

    @Select("select * from tb_user where username=#{username} and password=#{password}")
    User searchByUserNameAndPassword(@Param(value = "username") String username, @Param(value = "password") String password);

}
