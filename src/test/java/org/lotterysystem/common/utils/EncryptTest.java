package org.lotterysystem.common.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class EncryptTest {

    @Test
    void sha256Test() {
        String s = DigestUtil.sha256Hex("123456789");
        System.out.println(s);
    }

    //手机号对称加密
    @Test
    void aesTest() {
        // AES 密钥必须是 16 / 24 / 32 字节
        byte[] key = "1234567890123456".getBytes(StandardCharsets.UTF_8);
        AES aes = SecureUtil.aes(key);

        String plain = "13800138000";
        String cipher = aes.encryptHex(plain);
        String decrypted = aes.decryptStr(cipher);

        System.out.println("cipher = " + cipher);
        System.out.println("decrypted = " + decrypted);
    }
}
