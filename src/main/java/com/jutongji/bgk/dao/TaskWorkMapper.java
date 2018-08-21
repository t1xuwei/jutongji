package com.jutongji.bgk.dao;


import com.jutongji.bgk.model.TaskWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

@Mapper
public interface TaskWorkMapper extends tk.mybatis.mapper.common.Mapper<TaskWork>,MySqlMapper<TaskWork>{

    @Select("<script>"
            + "select count(*) from t_task_work where t_task_work.owner_type=#{ownerType} and t_task_work.owner_id=#{ownerId} and t_task_work.user_id=#{userId} "
            + "and deleted=#{deleted} and state in "
            + "<foreach item='state' index='index' collection='stateList' open='(' separator=',' close=')'>"
            + "#{state}"
            + "</foreach>"
            + "</script>")
    Integer countByOwnerTypeAndOwnerIdAndUserIdAndStateAndDeleted(@Param("ownerType") String ownerType, @Param("ownerId") Integer ownerId, @Param("userId") Integer userId, @Param("deleted") Integer deleted, @Param("stateList") List<Integer> stateList);


    List<TaskWork> selectByOwnerIdAndOwnerTypeAndDeleted(@Param("ownerId") Integer ownerId, @Param("ownerType") String ownerType, @Param("deleted") Integer deleted);

    List<TaskWork> selectByUserIdAndDeleted(@Param("userId") Integer userId, @Param("deleted") Integer deleted);

    Integer countByOwnerTypeAndOwnerIdAndHomeworkIdAndDeletedAndStateIn(@Param("ownerType") String ownerType, @Param("ownerId") Integer ownerId, @Param("homeworkId") Integer homeworkId, @Param("deleted") Integer deleted, @Param("stateList") List<Integer> stateList);

    Integer countByOwnerTypeAndOwnerIdAndHomeworkIdAndDeletedAndState(@Param("ownerType") String ownerType, @Param("ownerId") Integer ownerId, @Param("homeworkId") Integer homeworkId, @Param("deleted") Integer deleted, @Param("state") Integer state);

    @Select("<script>"
            + "select id from t_task_work where t_task_work.owner_type=#{ownerType} and t_task_work.owner_id=#{ownerId}  and t_task_work.type=#{type} and t_task_work.homework_id=#{homeworkId} and t_task_work.user_id=#{userId} "
            + "and deleted=#{deleted}"
            + "</script>")
    Integer selectByOwnerIdAndOwnerTypeAndHomeworkIdAndUserIdAndTypeAndDeleted(@Param("ownerId") Integer ownerId, @Param("ownerType") String ownerType, @Param("homeworkId") Integer homeworkId, @Param("userId") Integer userId, @Param("type") String type, @Param("deleted") Integer deleted);

    Integer updateById(@Param("taskWork") TaskWork taskWork, @Param("taskWorkId") Integer taskWorkId);


    @Select("<script>"
            + "select homework_id,homework_user_id,user_id from t_task_work where t_task_work.owner_type=#{ownerType} and t_task_work.owner_id=#{ownerId} "
            + "and deleted=#{deleted} and user_id in "
            + "<foreach item='userId' index='index' collection='userIds' open='(' separator=',' close=')'>"
            + "#{userId}"
            + "</foreach>"
            + "</script>")
    List<TaskWork> selectByOwnerIdAndOwnerTypeAndUserIdInAndDeleted(@Param("ownerId") Integer ownerId, @Param("ownerType") String ownerType, @Param("userIds") List<Integer> userIds, @Param("deleted") Integer deleted);
}