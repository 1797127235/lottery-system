package org.lotterysystem.dao.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.lotterysystem.dao.dataobject.Encrypt;
import org.lotterysystem.dao.dataobject.UserDo;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsertUser() {
        // Given
        UserDo userDo = new UserDo();
        userDo.setUserName("测试用户");
        userDo.setEmail("test@example.com");
        userDo.setPhoneNumber(new Encrypt("13800138000"));
        userDo.setPassword("e10adc3949ba59abbe56e057f20f883e"); // 123456的SHA256
        userDo.setIdentity("学生");

        // When
        userMapper.insert(userDo);

        // Then
        assertNotNull(userDo.getId());
        assertTrue(userDo.getId() > 0);
        
        // 打印插入的用户ID
        System.out.println("插入的用户ID: " + userDo.getId());
    }

    @Test
    void testCountByMail() {
        // Given
        String email = "test@example.com";

        // When
        int count = userMapper.countByMail(email);

        // Then
        System.out.println("邮箱 " + email + " 的数量: " + count);
        assertTrue(count >= 0);
    }

    @Test
    void testCountByPhone() {
        // Given
        Encrypt phoneNumber = new Encrypt("13800138000");

        // When
        int count = userMapper.countByPhone(phoneNumber);

        // Then
        System.out.println("手机号加密后的数量: " + count);
        assertTrue(count >= 0);
    }
}
