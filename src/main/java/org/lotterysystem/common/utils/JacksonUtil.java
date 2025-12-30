package org.lotterysystem.common.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.Callable;


/*
    封装序列化和反序列化方法使得不用
    手动处理异常
 */

public class JacksonUtil {

    private final static ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    private static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    private static <T> T tryParse(Callable<T> parse) {
        return tryParse(parse, JacksonException.class);
    }

    private static <T> T tryParse(Callable<T> callable, Class<? extends Exception> check) {
        try {
            return callable.call();
        } catch (Exception e) {
            if (check.isAssignableFrom(e.getClass())) {
                // 将受检异常包装成运行时异常，调用方无需显式捕获
                throw new RuntimeException(new JsonProcessingException(e.getMessage(), e) {});
            } else {
                throw new IllegalStateException(e);
            }
        }
    }


    //序列化方法
    public static String writeValueAsString(Object value){
        return JacksonUtil.tryParse(() ->
                JacksonUtil.getObjectMapper().writeValueAsString(value)
        );
    }

    //反序列化
    public static <T> T readValue(String value, Class<T> valueType){
        return JacksonUtil.tryParse(() ->
                JacksonUtil.getObjectMapper().readValue(value, valueType));
    }

    //反序列化list
    public static <T> T readListValue(String value,Class<?> paramClasses){
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, paramClasses);

        return JacksonUtil.tryParse(
                () -> JacksonUtil.getObjectMapper().readValue(value, javaType)
        );
    }
}
