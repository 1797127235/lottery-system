package org.lotterysystem.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.lotterysystem.dao.dataobject.ActivityPrizeDO;

@Mapper
public interface ActivityPrizeMapper {

    @Insert("insert into activity_prize (activity_id, prize_id, prize_amount, prize_tiers, status) values (#{ap.activityId}, #{ap.prizeId}, #{ap.prizeAmount}, #{ap.prizeTiers}, #{ap.status})")
    @Options(useGeneratedKeys = true, keyProperty = "ap.id", keyColumn = "id")
    int insert(@Param("ap") ActivityPrizeDO activityPrizeDO);
}
