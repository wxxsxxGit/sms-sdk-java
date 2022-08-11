package com.xsxx.sms.security;

import java.security.MessageDigest;

/**
 * MD5加密
 */
public class MD5Util {

    public static String MD5(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String str = "阿里";
        System.out.println(MD5(str));
        System.out.println("5ac0b6e56fba67ca6c16f36e694563fc".equalsIgnoreCase(MD5(str)));
    }
}
