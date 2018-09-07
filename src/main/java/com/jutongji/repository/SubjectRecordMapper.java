package com.jutongji.repository;

import com.jutongji.custommapper.CustomMapper;
import com.jutongji.model.SubjectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubjectRecordMapper extends CustomMapper<SubjectRecord>{

    @Select("select * from t_subject_record where subject_id=#{subjectId} and deleted=#{deleted}")
    List<SubjectRecord> selectBySubjectIdAndDeltedFalse(@Param("subjectId") Integer subjectId, @Param("deleted")Integer deleted);
}