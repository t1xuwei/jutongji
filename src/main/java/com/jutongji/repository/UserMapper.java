package com.jutongji.repository;

import com.jutongji.custommapper.CustomMapper;
import com.jutongji.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by wei on 2017/7/18.
 */
@Mapper
public interface UserMapper extends CustomMapper<User>{

    @Select("select * from tb_user where userName=#{userName}")
    public User findByName(String username);


    @Update("update tb_user set password=#{password} where userName =#{userName} ")
    public void changePwdByPhone(@Param(value = "userName") String phone, @Param(value = "password") String password);

    @Select("select * from tb_user where userName=#{userName} and password=#{password}")
    User searchByUserNameAndPassword(@Param(value = "userName") String username, @Param(value = "password") String password);

}
