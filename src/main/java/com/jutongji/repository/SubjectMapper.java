package com.jutongji.repository;

import com.jutongji.custommapper.CustomMapper;
import com.jutongji.model.Subject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Mapper
public interface SubjectMapper extends CustomMapper<Subject>{

    @Select("select count(*) from t_subject where created_by=#{createdBy} and deleted=#{deleted}")
    Integer countByCreatedByAndDeletedFalse(@Param("createdBy") Integer createdBy,@Param("deleted") Integer deleted);

    @Update("update t_subject set deleted=#{deteded} where id = #{id}")
    void updateDeletedById(@Param("deleted")Integer deleted, @Param("id") Integer id);

    @Select("select * from t_subject where created_by=#{createdBy} and deleted=#{deleted}")
    List<Subject> selectByCreatedByAndDeletedFalse(Integer userId, Integer notDelted);
}