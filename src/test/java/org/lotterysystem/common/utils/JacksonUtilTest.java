package org.lotterysystem.common.utils;

import org.junit.jupiter.api.Test;
import org.lotterysystem.common.pojo.CommonResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JacksonUtilTest {

    @Test
    void writeValueAsString_shouldSerializeObject() {
        CommonResult<String> payload = CommonResult.success("ok");

        String json = JacksonUtil.writeValueAsString(payload);

        assertNotNull(json);
        assertTrue(json.contains("\"code\""));
        assertTrue(json.contains("\"data\""));
        assertTrue(json.contains("ok"));
    }

    @Test
    void readValue_shouldDeserializeToType() {
        String json = "{\"code\":200,\"message\":\"success\",\"data\":\"ok\"}";

        CommonResult<?> result = JacksonUtil.readValue(json, CommonResult.class);

        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals("ok", result.getData());
    }

    @Test
    void readListValue_shouldDeserializeToList() {
        List<String> source = Arrays.asList("a", "b", "c");
        String json = JacksonUtil.writeValueAsString(source);

        List<String> target = JacksonUtil.readListValue(json, String.class);

        assertEquals(source, target);
    }

    @Test
    void tryParse_shouldWrapJacksonExceptionAsRuntime() {
        // readValue with invalid json triggers JacksonException and should be wrapped as RuntimeException
        String invalidJson = "not-json";

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> JacksonUtil.readValue(invalidJson, CommonResult.class));

        assertNotNull(ex.getCause());
        assertTrue(ex.getCause() instanceof Exception);
    }
}
