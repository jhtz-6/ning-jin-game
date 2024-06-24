package com.myf.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Author: myf
 * @CreateTime: 2024-05-18  10:50
 * @Description: TODO
 */
@Slf4j
public class MD5Utils {

    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    // 加密
    public static String encrypt(String plainText, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 解密
    public static String decrypt(String encryptedText, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, CHARSET);
    }
}
