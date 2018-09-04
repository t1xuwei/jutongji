package com.jutongji.service.impl;

import com.jutongji.config.CommonConstants;
import com.jutongji.model.Subject;
import com.jutongji.model.SubjectRecord;
import com.jutongji.repository.SubjectMapper;
import com.jutongji.repository.SubjectRecordMapper;
import com.jutongji.service.ISubjectService;
import com.jutongji.util.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/9/4 16:09
 */
@Service
@Transactional
public class SubjectServiceImpl implements ISubjectService{
    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectRecordMapper subjectRecordMapper;

    @Override
    public Data<?> save(Subject subject) {
        Integer count = subjectMapper.countByCreatedByAndDeletedFalse(subject.getCreatedBy(), CommonConstants.NOT_DELTED);
        if(count > CommonConstants.MAX_SUBJECT_NUM){
            return Data.failure("当前系统每个用户最多只允许创建十个主题");
        }
        subjectMapper.insertUseGeneratedKeys(subject);
        return Data.success();
    }

    @Override
    public Data<?> delSubjectById(Integer userId, Integer id) {
        Subject subject = subjectMapper.selectByPrimaryKey(id);
        if(subject == null || !subject.getCreatedBy().equals(userId))
            return Data.failure("没有权限！");
        subjectMapper.updateDeletedById(CommonConstants.DELTED, id);
        return Data.success();
    }

    @Override
    public Data<List<Subject>> selectByUserId(Integer userId) {
        List<Subject> subjectList = subjectMapper.selectByCreatedByAndDeletedFalse(userId, CommonConstants.NOT_DELTED);
        return Data.success(subjectList);
    }

    @Override
    public Data<?> saveRecord(SubjectRecord subjectRecord) {
        subjectRecordMapper.insertUseGeneratedKeys(subjectRecord);
        return Data.success();
    }

    @Override
    public Data<?> selectRecordsById(Integer subjectId, Integer userId) {
        SubjectRecord subjectRecord = subjectRecordMapper.selectByPrimaryKey(subjectId);
        if(subjectRecord == null || !userId.equals(subjectRecord.getCreatedById())){
            return Data.failure("没有权限！");
        }
        List<SubjectRecordMapper> subjectRecords = subjectRecordMapper.selectBySubjectIdAndDeltedFalse(subjectId, CommonConstants.NOT_DELTED);
        return Data.success(subjectRecords);
    }


}
