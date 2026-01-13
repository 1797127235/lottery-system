package org.lotterysystem.dao.mapper;

import org.apache.ibatis.annotations.*;
import org.lotterysystem.dao.dataobject.ActivityUserDO;

import java.util.List;

@Mapper
public interface ActivityUserMapper {

    @Insert("insert into activity_user (activity_id, user_id, user_name, status) values (#{au.activityId}, #{au.userId}, #{au.userName}, #{au.status})")
    @Options(useGeneratedKeys = true, keyProperty = "au.id", keyColumn = "id")
    int insert(@Param("au") ActivityUserDO activityUserDO);

    @Insert({
            "<script>",
            "INSERT INTO activity_user (activity_id, user_id, user_name, status)",
            "VALUES",
            "<foreach collection='list' item='au' separator=','>",
            "(#{au.activityId}, #{au.userId}, #{au.userName}, #{au.status})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "list.id", keyColumn = "id")
    int batchInsert(@Param("list") List<ActivityUserDO> activityUserDOList);

    @Select({
            "select id, activity_id, user_id, user_name, status, gmt_create, gmt_modified",
            "from activity_user",
            "where activity_id = #{activityId}"
    })
    @Results(id = "activityUserResultMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "status", property = "status"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<ActivityUserDO> selectByActivityId(@Param("activityId") Long activityId);

    /**
     * 批量更新活动下指定用户状态
     */
    @Update({
            "<script>",
            "update activity_user",
            "set status = #{status}, gmt_modified = now()",
            "where activity_id = #{activityId}",
            "and user_id in",
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    int updateStatus(@Param("activityId") Long activityId,
                     @Param("userIds") List<Long> userIds,
                     @Param("status") String status);

    @Select({
            "<script>",
            "select id, activity_id, user_id, user_name, status, gmt_create, gmt_modified",
            "from activity_user",
            "where activity_id = #{activityId}",
            "and user_id in",
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    @ResultMap("activityUserResultMap")
    List<ActivityUserDO> batchSelectByAUIds(@Param("activityId") Long activityId,
                                            @Param("userIds") List<Long> userIds);
}
