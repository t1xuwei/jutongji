package com.jutongji.repository;

import com.jutongji.custommapper.CustomMapper;
import com.jutongji.model.UserSubject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSubjectMapper extends CustomMapper<UserSubject>{

}