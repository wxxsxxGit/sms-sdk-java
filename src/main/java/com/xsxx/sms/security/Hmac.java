package com.xsxx.sms.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author SpiderMan
 * @Date: 2020/4/7
 */
public class Hmac {

    public static String createSignature(long timeStamp, String body, String key){
        return hmac(body + timeStamp, key);
    }

    private static String hmac(String message, String key) {
        String result = null;
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256.init(secretKey);
            result = Base64.getEncoder().encodeToString(sha256.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
    }

}
