package org.lotterysystem.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.lotterysystem.dao.dataobject.ActivityUserDO;

@Mapper
public interface ActivityUserMapper {

    @Insert("insert into activity_user (activity_id, user_id, user_name, status) values (#{au.activityId}, #{au.userId}, #{au.userName}, #{au.status})")
    @Options(useGeneratedKeys = true, keyProperty = "au.id", keyColumn = "id")
    int insert(@Param("au") ActivityUserDO activityUserDO);
}
