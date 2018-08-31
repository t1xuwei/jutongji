package com.jutongji.repository;

import com.jutongji.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface UserRepository extends JpaBaseRepository<User, Integer>
{
    User findByEmail(String email);

    User save(User user);

    @Select("select * from t_user where id = userId")
    User findByUserId(@Param("userId") Integer userId);
}
