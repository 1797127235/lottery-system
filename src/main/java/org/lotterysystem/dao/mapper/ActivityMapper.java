package org.lotterysystem.dao.mapper;

import org.apache.ibatis.annotations.*;
import org.lotterysystem.dao.dataobject.ActivityDO;

import java.util.List;

@Mapper
public interface ActivityMapper {

    @Insert("insert into activity (activity_name, description, status) values (#{a.activityName}, #{a.description}, #{a.status})")
    @Options(useGeneratedKeys = true, keyProperty = "a.id", keyColumn = "id")
    int insert(@Param("a") ActivityDO activityDO);

    @Select("select count(1) from activity")
    int count();

    @Select({
            "select id, activity_name, description, status, gmt_create, gmt_modified",
            "from activity",
            "where id = #{id}"
    })
    @ResultMap("activityResultMap")
    ActivityDO selectById(@Param("id") Long id);

    @Select({
            "select id, activity_name, description, status, gmt_create, gmt_modified",
            "from activity",
            "order by id desc",
            // postgres 语法：limit pageSize offset offset
            "limit #{pageSize} offset #{offset}"
    })
    @Results(id = "activityResultMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "activity_name", property = "activityName"),
            @Result(column = "description", property = "description"),
            @Result(column = "status", property = "status"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<ActivityDO> selectActivityList(@Param("offset") int offset, @Param("pageSize") int pageSize);

}
