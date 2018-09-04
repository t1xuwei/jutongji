package com.jutongji.service;

import com.jutongji.model.Subject;
import com.jutongji.model.SubjectRecord;
import com.jutongji.util.Data;

import java.util.List;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/9/4 16:09
 */
public interface ISubjectService {
    Data<?> save(Subject subject);

    Data<?> delSubjectById(Integer userId, Integer id);

    Data<List<Subject>> selectByUserId(Integer userId);

    Data<?> saveRecord(SubjectRecord subjectRecord);

    Data<?> selectRecordsById(Integer subjectId, Integer userId);
}
