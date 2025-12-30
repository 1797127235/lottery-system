package org.lotterysystem.dao.mapper;

import org.apache.ibatis.annotations.*;
import org.lotterysystem.dao.dataobject.PrizeDO;

import java.util.List;

@Mapper
public interface PrizeMapper {

    @Insert("insert into prize (name, description, price, image_url) values (#{p.name}, #{p.description}, #{p.price}, #{p.imageUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "p.id", keyColumn = "id")
    int insert(@Param("p") PrizeDO prizeDO);

    @Select("select count(1) from prize")
    int count();

    @Select("select id, name, description, price, image_url, gmt_create, gmt_modified from prize order by id desc limit #{offset}, #{pageSize}")
    @Results(id = "prizeResultMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "name", property = "name"),
            @Result(column = "description", property = "description"),
            @Result(column = "price", property = "price"),
            @Result(column = "image_url", property = "imageUrl"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<PrizeDO> selectPrizeList(@Param("offset") int offset, @Param("pageSize") int pageSize);
}
