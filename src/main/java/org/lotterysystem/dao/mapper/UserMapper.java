package org.lotterysystem.dao.mapper;


import org.apache.ibatis.annotations.*;
import org.lotterysystem.dao.dataobject.Encrypt;
import org.lotterysystem.dao.dataobject.UserDo;

import java.util.List;

@Mapper
public interface UserMapper {
    // 获取邮箱绑定人数
    @Select("select count(*) from \"user\" where email = #{email}")
    int countByMail(@Param("email") String email);

    //获取手机号
    @Select("select count(*) from \"user\" where phone_number = #{phoneNumber}")
    int countByPhone(@Param("phoneNumber") Encrypt phoneNumber);

    // 登录：按邮箱查询
    @Select("select * from \"user\" where email = #{email}")
    UserDo selectByMail(@Param("email") String email);

    // 登录：按手机号查询
    @Select("select * from \"user\" where phone_number = #{phoneNumber}")
    UserDo selectByPhone(@Param("phoneNumber") Encrypt phoneNumber);

    @Insert("insert into \"user\" (user_name, email, phone_number, password, identity) values (#{userName}, #{email}, #{phoneNumber}, #{password}, #{identity})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void insert(UserDo userDo);

    @Select("select * from \"user\" where identity = #{identity}")
    java.util.List<UserDo> selectByIdentity(@Param("identity") String identity);

    @Select({
            "<script>",
            "select id from \"user\"",
            "<where>",
            "  <if test=\"list != null and list.size() &gt; 0\">",
            "    id in",
            "    <foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\">",
            "      #{id}",
            "    </foreach>",
            "  </if>",
            "  <if test=\"list == null or list.size() == 0\">",
            "    and 1 = 0",
            "  </if>",
            "</where>",
            "</script>"
    })
    List<Long> selectExistByids(@Param("list") List<Long> ids);
}
