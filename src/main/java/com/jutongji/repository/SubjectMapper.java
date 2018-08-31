package com.jutongji.repository;

import com.jutongji.custommapper.CustomMapper;
import com.jutongji.model.Subject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubjectMapper extends CustomMapper<Subject>{

}