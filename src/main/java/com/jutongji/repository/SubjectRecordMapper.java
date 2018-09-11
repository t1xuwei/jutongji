package com.jutongji.repository;

import com.jutongji.custommapper.CustomMapper;
import com.jutongji.model.SubjectRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface SubjectRecordMapper extends CustomMapper<SubjectRecord>{

    @Select("select * from t_subject_record where subject_id=#{subjectId} and deleted=#{deleted}")
    List<SubjectRecord> selectBySubjectIdAndDeltedFalse(@Param("subjectId") Integer subjectId, @Param("deleted")Integer deleted);

    @Select("<script> select * from t_subject_record where subject_id=#{subjectId} and deleted=#{deleted} and created_at &gt;= #{timeStart} and created_at &lt;= #{timeEnd} </script>")
    List<SubjectRecord> selectBySubjectIdAndCreatedByIdAndDeltedAndTimeSlotFalse(@Param("subjectId") Integer subjectId,@Param("timeStart") Date timeStart,@Param("timeEnd") Date timeEnd,@Param("deleted") Integer deleted);
}