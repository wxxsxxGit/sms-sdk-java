package com.xsxx.sms.util;

import cn.hutool.core.codec.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AesUtil {
    private static final String KEY_ALGORITHM = "AES";

    public static String aesEncode(String secretKey, String content) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, key);
        byte[] byteEncode = content.getBytes("utf-8");
        return Base64.encode(cipher.doFinal(byteEncode));
    }

    private static SecretKeySpec getSecretKey(String password) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kg.init(128, secureRandom);
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    /**
     * 解密
     *
     * @param secretKey 秘钥
     * @param content   解密串
     * @return
     */
    public static String aesDecode(String secretKey, String content) throws Exception {
        SecretKey key = getSecretKey(secretKey);
        //6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.DECRYPT_MODE, key);
        //8.将加密并编码后的内容解码成字节数组
        byte[] byteContent = new BASE64Decoder().decodeBuffer(content);
        /*
         * 解密
         */
        byte[] byteDecode = cipher.doFinal(byteContent);
        return new String(byteDecode, "utf-8");
    }
}