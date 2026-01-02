package org.lotterysystem.dao.mapper;

import org.apache.ibatis.annotations.*;
import org.lotterysystem.dao.dataobject.ActivityPrizeDO;

import java.util.List;

@Mapper
public interface ActivityPrizeMapper {

    @Insert("insert into activity_prize (activity_id, prize_id, prize_amount, prize_tiers, status) values (#{ap.activityId}, #{ap.prizeId}, #{ap.prizeAmount}, #{ap.prizeTiers}, #{ap.status})")
    @Options(useGeneratedKeys = true, keyProperty = "ap.id", keyColumn = "id")
    int insert(@Param("ap") ActivityPrizeDO activityPrizeDO);

    @Insert({
            "<script>",
            "INSERT INTO activity_prize (activity_id, prize_id, prize_amount, prize_tiers, status)",
            "VALUES",
            "<foreach collection='list' item='ap' separator=','>",
            "(#{ap.activityId}, #{ap.prizeId}, #{ap.prizeAmount}, #{ap.prizeTiers}, #{ap.status})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "list.id", keyColumn = "id")
    int batchInsert(@Param("list") List<ActivityPrizeDO> activityPrizeDOList);

    @Select({
            "select id, activity_id, prize_id, prize_amount, prize_tiers, status, gmt_create, gmt_modified",
            "from activity_prize",
            "where activity_id = #{activityId}"
    })
    @Results(id = "activityPrizeResultMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "activity_id", property = "activityId"),
            @Result(column = "prize_id", property = "prizeId"),
            @Result(column = "prize_amount", property = "prizeAmount"),
            @Result(column = "prize_tiers", property = "prizeTiers"),
            @Result(column = "status", property = "status"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<ActivityPrizeDO> selectByActivityId(@Param("activityId") Long activityId);
}
