package org.lotterysystem.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.lotterysystem.dao.mapper.UserMapper;
import org.lotterysystem.dao.dataobject.Encrypt;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private Method checkMailUserMethod;
    private Method checkPhoneNumberUsedMethod;

    @BeforeEach
    void setUp() throws Exception {
        checkMailUserMethod = UserServiceImpl.class.getDeclaredMethod("checkMailUser", String.class);
        checkMailUserMethod.setAccessible(true);
        
        checkPhoneNumberUsedMethod = UserServiceImpl.class.getDeclaredMethod("checkPhoneNumberUsed", String.class);
        checkPhoneNumberUsedMethod.setAccessible(true);
    }

    @Test
    void testCheckMailUser_WhenEmailNotExists_ShouldReturnTrue() throws Exception {
        // Given
        String email = "test@example.com";
        when(userMapper.countByMail(email)).thenReturn(0);

        // When
        boolean result = (boolean) checkMailUserMethod.invoke(userService, email);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckMailUser_WhenEmailExists_ShouldReturnFalse() throws Exception {
        // Given
        String email = "existing@example.com";
        when(userMapper.countByMail(email)).thenReturn(1);

        // When
        boolean result = (boolean) checkMailUserMethod.invoke(userService, email);

        // Then
        assertFalse(result);
    }

    @Test
    void testCheckMailUser_WhenMultipleEmailsExist_ShouldReturnFalse() throws Exception {
        // Given
        String email = "duplicate@example.com";
        when(userMapper.countByMail(email)).thenReturn(2);

        // When
        boolean result = (boolean) checkMailUserMethod.invoke(userService, email);

        // Then
        assertFalse(result);
    }

    @Test
    void testCheckMailUser_WithNullEmail_ShouldReturnTrue() throws Exception {
        // Given
        String email = null;
        when(userMapper.countByMail(email)).thenReturn(0);

        // When
        boolean result = (boolean) checkMailUserMethod.invoke(userService, (Object) email);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckMailUser_WithEmptyEmail_ShouldReturnTrue() throws Exception {
        // Given
        String email = "";
        when(userMapper.countByMail(email)).thenReturn(0);

        // When
        boolean result = (boolean) checkMailUserMethod.invoke(userService, email);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckPhoneNumberUsed_WhenPhoneNotExists_ShouldReturnTrue() throws Exception {
        // Given
        String phoneNumber = "13800138000";
        when(userMapper.countByPhone(any(Encrypt.class))).thenReturn(0);

        // When
        boolean result = (boolean) checkPhoneNumberUsedMethod.invoke(userService, phoneNumber);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckPhoneNumberUsed_WhenPhoneExists_ShouldReturnFalse() throws Exception {
        // Given
        String phoneNumber = "13800138001";
        when(userMapper.countByPhone(any(Encrypt.class))).thenReturn(1);

        // When
        boolean result = (boolean) checkPhoneNumberUsedMethod.invoke(userService, phoneNumber);

        // Then
        assertFalse(result);
    }

    @Test
    void testCheckPhoneNumberUsed_WhenMultiplePhonesExist_ShouldReturnFalse() throws Exception {
        // Given
        String phoneNumber = "13800138002";
        when(userMapper.countByPhone(any(Encrypt.class))).thenReturn(2);

        // When
        boolean result = (boolean) checkPhoneNumberUsedMethod.invoke(userService, phoneNumber);

        // Then
        assertFalse(result);
    }

    @Test
    void testCheckPhoneNumberUsed_WithNullPhone_ShouldReturnTrue() throws Exception {
        // Given
        String phoneNumber = null;
        when(userMapper.countByPhone(any(Encrypt.class))).thenReturn(0);

        // When
        boolean result = (boolean) checkPhoneNumberUsedMethod.invoke(userService, (Object) phoneNumber);

        // Then
        assertTrue(result);
    }

    @Test
    void testCheckPhoneNumberUsed_WithEmptyPhone_ShouldReturnTrue() throws Exception {
        // Given
        String phoneNumber = "";
        when(userMapper.countByPhone(any(Encrypt.class))).thenReturn(0);

        // When
        boolean result = (boolean) checkPhoneNumberUsedMethod.invoke(userService, phoneNumber);

        // Then
        assertTrue(result);
    }
}
