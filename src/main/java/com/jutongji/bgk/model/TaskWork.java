package com.jutongji.bgk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="t_task_work")
public class TaskWork {

    //完成作业为 主题 所有
    public static final String TYPE_SUBJECT = "t_training_subject";
    //完成作业为课程所有
    public static final String TYPE_COURSE = "t_training_course";
    //作业状态未提交
    public static final Integer STATE_NO_COMMIT = Integer.valueOf(0);
    //作业状态已提交
    public static final Integer STATE_COMMIT = Integer.valueOf(1);
    //作业状态已批改
    public static final Integer STATE_CORRECT = Integer.valueOf(2);
    //删除
    public static final Integer DELETED_YES = Integer.valueOf(1);
    //有效
    public static final Integer DELETED_NO = Integer.valueOf(0);

    private static final List<Integer> COMMIT_LIST = new ArrayList<>();

    static{
        COMMIT_LIST.add(STATE_COMMIT);
        COMMIT_LIST.add(STATE_CORRECT);
    }

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @ApiModelProperty("标准作业id 作业系统中的homework_id")
    @Column(name="homework_id")
    private Integer homeworkId;

    @ApiModelProperty("作业名称")
    @Column(name="name")
    private String name;

    @ApiModelProperty("作业类型 笔试作业:note 视频作业:video")
    @Column(name="type")
    private String type;

    @ApiModelProperty("属于主题还是课程 主题：t_training_subjcet 课程：t_training_course")
    @Column(name="owner_type")
    private String ownerType;

    @ApiModelProperty("主题id或者课程id")
    @Column(name="owner_id")
    private Integer ownerId;

    @ApiModelProperty("用户id")
    @Column(name="user_id")
    private Integer userId;

    @ApiModelProperty("用户姓名")
    @Column(name="user_name")
    private String userName;

    @ApiModelProperty("创建者")
    @Column(name="created_by")
    private String createdBy;

    @ApiModelProperty("创建时间")
    @Column(name="created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @ApiModelProperty("提交作业时间")
    @Column(name="submit_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    @ApiModelProperty("用户作业id  homework_user_id")
    @Column(name="homework_user_id")
    private Integer homeworkUserId;

    @ApiModelProperty("视频路径,提交视频作业时为必填项")
    @Column(name="video_path")
    private String videoPath;

    @ApiModelProperty("作业状态 1:已提交 2:已批改 ")
    @Column(name="state")
    private Integer state;

    public Integer getHomeworkUserId() {
        return homeworkUserId;
    }

    public void setHomeworkUserId(Integer homeworkUserId) {
        this.homeworkUserId = homeworkUserId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Integer homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public static List<Integer> getCommitList() {
        return COMMIT_LIST;
    }
}