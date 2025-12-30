package org.lotterysystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.lotterysystem.common.pojo.CommonResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JacksonTest {

    @Test
    void jacksonTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        CommonResult<String> commonResult = CommonResult.error(500,"系统错误");
        String str;
        //序列化

        str = objectMapper.writeValueAsString(commonResult);
        System.out.println(str);


        //反序列化
        CommonResult<?> commonResult1 = objectMapper.readValue(str, CommonResult.class);
        System.out.println(commonResult1);

        //List 序列化
        List<CommonResult<String>> list = Arrays.asList(
                CommonResult.success("success1"),
                CommonResult.success("success2")
        );
        str = objectMapper.writeValueAsString(list);
        System.out.println(str);

        //List 反序列化
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, CommonResult.class);

        list = objectMapper.readValue(str, javaType);

        list.forEach(System.out::println);


    }
}
