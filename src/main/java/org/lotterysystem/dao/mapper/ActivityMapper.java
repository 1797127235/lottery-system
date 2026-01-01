package org.lotterysystem.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.lotterysystem.dao.dataobject.ActivityDO;

@Mapper
public interface ActivityMapper {

    @Insert("insert into activity (activity_name, description, status) values (#{a.activityName}, #{a.description}, #{a.status})")
    @Options(useGeneratedKeys = true, keyProperty = "a.id", keyColumn = "id")
    int insert(@Param("a") ActivityDO activityDO);


}
