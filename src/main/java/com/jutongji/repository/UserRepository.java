package com.jutongji.repository;

import com.jutongji.model.User;


public interface UserRepository extends JpaBaseRepository<User, Integer>
{
    User findByEmail(String email);

    User save(User user);
}
