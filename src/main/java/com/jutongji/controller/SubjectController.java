package com.jutongji.controller;

import com.jutongji.config.PathConfig;
import com.jutongji.model.Subject;
import com.jutongji.model.SubjectRecord;
import com.jutongji.service.ISubjectService;
import com.jutongji.session.UserSession;
import com.jutongji.session.UserSessionFactory;
import com.jutongji.util.Data;
import com.jutongji.util.TimeUtil;
import com.jutongji.vo.RecordQueryParam;
import com.jutongji.vo.SubjectRecordDateVO;
import io.swagger.annotations.ApiOperation;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author: xuw
 * @Description:
 * @Date: 2018/8/31 14:24
 */
@Controller
public class SubjectController {

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private PathConfig pathConfig;

    @ApiOperation("添加主题页面")
    @GetMapping("/subject/addView")
    public String addSubjectView(){
        return "subject/add_subject";
    }

    @ApiOperation("添加主题")
    @ResponseBody
    @PostMapping("/subject/add")
    public Data<?> addSubject(Subject subject, HttpSession session){
        UserSession userSession = UserSessionFactory.getUserSession(session);
        if(null == userSession){
            return Data.failure("用户未登录。");
        }
        subject.setCreatedBy(userSession.getUserId());
        subject.setCreatedAt(new Date());
        subject.setUpdatedAt(new Date());
        return subjectService.save(subject);
    }

    @ApiOperation("删除主题")
    @GetMapping("/subject/{id}/del")
    public Data<?> delSubject(@PathVariable("id")Integer id, HttpSession session){
        UserSession userSession = UserSessionFactory.getUserSession(session);
        if(null == userSession){
            return Data.failure("用户未登录。");
        }
        return subjectService.delSubjectById(userSession.getUserId(), id);
    }

    @ApiOperation("查看主题记录")
    @GetMapping("/subject/listView")
    public String subjectList(HttpSession session, Model model){
        UserSession userSession = UserSessionFactory.getUserSession(session);
        if(null == userSession){
            return String.format("redirect:%suser/login", pathConfig.getWebsitePath()) ;
        }
        List<Subject> subjectList =  subjectService.selectByUserId(userSession.getUserId()).getData();
        model.addAttribute("list", subjectList);
        return "subject/list";
    }

    @ApiOperation("添加主题的记录")
    @ResponseBody
    @PostMapping("/record/add")
    public Data<?> addSubjectRecord(@RequestBody SubjectRecord subjectRecord, HttpSession session){
        UserSession userSession = UserSessionFactory.getUserSession(session);
        if(null == userSession){
            return Data.failure("用户未登录");
        }
        subjectRecord.setCreatedById(userSession.getUserId());
        return subjectService.saveRecord(subjectRecord);
    }

    @ApiOperation("删除记录")
    @GetMapping("/record/{id}/del")
    public Data<?> delRecord(@PathVariable("id") Integer id, HttpSession session){
        UserSession userSession = UserSessionFactory.getUserSession(session);
        if(null == userSession){
            return Data.failure("用户未登录");
        }
        return Data.success();
    }

    @ApiOperation("根据主题id查询记录")
    @GetMapping("/subject/{id}/records")
    public String recordList(@PathVariable(name = "id") Integer subjectId, HttpSession session, Model model){
        UserSession userSession = UserSessionFactory.getUserSession(session);
        if(null == subjectId){
            return "404";
        }
        if(null == userSession){
            return String.format("redirect:%suser/login", pathConfig.getWebsitePath()) ;
        }
        List<SubjectRecord> records = (List<SubjectRecord>) subjectService.selectRecordsById(subjectId, userSession.getUserId()).getData();
        model.addAttribute("resultList",records);
        model.addAttribute("totalCount",records.size());
        model.addAttribute("subjectId", subjectId);
        return "subject/record/list";
    }

    @ApiOperation("根据时间查询记录")
    @ResponseBody
    @PostMapping("/subject/records")
    public Data<?> recordList(@RequestBody RecordQueryParam recordQueryParam, HttpSession session, Model model){
        UserSession userSession = UserSessionFactory.getUserSession(session);
        if(null == userSession){
            return Data.failure("没有登录");
        }
        Subject subject = subjectService.selectByPrimaryKey(recordQueryParam.getSubjectId());
        if(!subject.getCreatedBy().equals(userSession.getUserId())){
            return Data.failure("没有权限查看其他人的主题记录");
        }
        recordQueryParam.setMonthVal(RecordQueryParam.monthMap.get(recordQueryParam.getMonth()));
        List<SubjectRecord> records =  subjectService.selectRecordsBySubjectIdAndUserIdAndTime(recordQueryParam, userSession.getUserId());
        List<SubjectRecordDateVO> result = new ArrayList();
        records.forEach((x) -> {
            SubjectRecordDateVO record = new SubjectRecordDateVO();
            record.setCreatedAt(TimeUtil.formatDate(x.getCreatedAt(),TimeUtil.SINGLE_MONTH));
            record.setDesc(x.getDesc());
            result.add(record);
        });
        return Data.success(result);
    }


}
