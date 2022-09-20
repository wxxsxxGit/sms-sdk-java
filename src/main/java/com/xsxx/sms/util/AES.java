package com.xsxx.sms.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Administrator
 */
public class AES {
    private final static int AES_KEY_LENGTH = 16;

    /**
     * 默认KEY填充算法
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * "算法/模式/补码方式"
     */
    private static final String KEY_ALGORITHM_CIPHER = "AES/ECB/PKCS5Padding";

    /**
     * AES 密钥长度规定为16位，这里判断spKey
     * 1、长度不满足16位的统一前置填充字符'a
     * 2、大于长度的统一截取前16位
     *
     * @param spKey 密钥
     * @return 16位密钥字节数组
     */
    private static byte[] getBytesBySpkey(String spKey) {
        if (spKey.length() >= AES_KEY_LENGTH) {
            return StrUtil.sub(spKey, 0, 16).getBytes(CharsetUtil.CHARSET_UTF_8);
        }
        return StrUtil.fill(spKey, 'a', AES_KEY_LENGTH, true).getBytes(CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 加密
     *
     * @param sSrc  待加密字符串
     * @param spKey 密钥
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String sSrc, String spKey) throws Exception {
        byte[] raw = getBytesBySpkey(spKey);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     * 解密
     *
     * @param sSrc 待解密字符串
     * @param spKey 密钥
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String sSrc, String spKey) throws Exception {
        try {
            byte[] raw = getBytesBySpkey(spKey);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "httpSendUser0400";
        // 需要加密的字串
        String cSrc = "{\"content\":\"【线上线下3】333您的验证码为123456，在10分钟内有效。\",\"mobile\":\"13814269825\",\"extCode\":\"333456\",\"msgId\":\"8629637681836384963\",\"sId\":\"123456789abcdefg\"}";
        System.out.println(cSrc);
        // 加密
        String enString = AES.aesEncrypt(cSrc, cKey);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AES.aesDecrypt(enString, cKey);
        System.out.println("解密后的字串是：" + DeString);
    }
}