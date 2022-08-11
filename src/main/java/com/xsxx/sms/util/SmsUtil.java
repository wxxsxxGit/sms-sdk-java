package com.xsxx.sms.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xsxx.sms.BaseApi;
import com.xsxx.sms.model.BatchSubmitResp;
import com.xsxx.sms.model.DeliverResp;
import com.xsxx.sms.model.ReportResp;
import com.xsxx.sms.model.Sms;
import com.xsxx.sms.model.SubmitResp;
import com.xsxx.sms.security.MD5Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 工具类
 *
 * @author momo
 * @date 2020-01-02 13:15:15
 */
public class SmsUtil {

    /**
     * 签名最大长度
     */
    public final static int SIGN_MAX_LENGTH = 20;

    /**
     * 内容最大长度
     */
    public final static int CONTENT_MAX_LENGTH = 1000;

    /**
     * 是否是手机号
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone){
        return phone != null && phone.length() == 11 && phone.startsWith("1");
    }

    /**
     * 是否是链接
     * @param url
     * @return
     */
    public static boolean isURL(String url){
        return  url != null && url.length() > 10 && (url.startsWith("http://") || url.startsWith("https://"));
    }

    /**
     * 去除手机号码前缀
     * @param phone  需要确保phone不为null
     * @return
     */
    public static String removePhonePrefix(String phone){
        if (phone.startsWith("1")) {
            return phone;
        } else if (phone.startsWith("86")) {
            return phone.substring(2);
        } else if (phone.startsWith("+86")) {
            return phone.substring(3);
        } else if (phone.startsWith("0086")) {
            return phone.substring(4);
        }
        return phone;
    }

    /**
     * 检查字符是否包含表情符
     * @param content
     * @return
     */
    public static boolean hasEmojiFourChar(String content) {
        if(content == null){
            return false;
        }
        byte[] conbyte = content.getBytes(Charset.forName("UTF8"));
        for (int i = 0; i < conbyte.length; i++) {
            if ((conbyte[i] & 0xF8) == 0xF0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 替换四个字节的字符 '\xF0\x9F\x98\x84）的解决方案 ??
     *
     * UTF-8是一种变长字节编码方式。对于某一个字符的UTF-8编码，如果只有一个字节则其最高二进制位为0；
     * 如果是多字节，其第一个字节从最高位开始，连续的二进制位值为1的个数决定了其编码的位数，其余各字节均以10开头。
     * UTF-8最多可用到6个字节。❤`
     * 如表：
     * 1字节 0xxxxxxx
     * 2字节 110xxxxx 10xxxxxx
     * 3字节 1110xxxx 10xxxxxx 10xxxxxx
     * 4字节 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
     * 5字节 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
     * 6字节 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
     * @param content
     * @return
     */
    public static final byte[] EMOJI_FIX = new byte[]{(byte)0xE2,(byte)0x9D,(byte)0xA4,(byte)0x60};

    public static String removeEmojiFourChar(String content) {
        if(content == null){
            return null;
        }
        byte[] conbyte = content.getBytes(Charset.forName("UTF8"));
        boolean hasEmoji = false;
        for (int i = 0; i < conbyte.length; i++) {
            if ((conbyte[i] & 0xF8) == 0xF0) {
                hasEmoji = true;
                for (int j = 0; j < 4; j++) {
                    if( i+j < conbyte.length){
                        conbyte[i+j] = EMOJI_FIX[j];
                    }
                }
                i += 3;
            }
        }
        if(hasEmoji){
            try {
                return new String(conbyte,"UTF8");
            } catch (UnsupportedEncodingException e) {

            }
        }
        return content;
    }

    /**
     * 获取签名两个括号的索引位置
     * @param content
     * @return
     */
    public static int[] getSignPosition(String content){
        if(content == null){
            return null;
        }
        // 签名前置的情况
        int lastIndex = content.length() - 1, start = -1, end = -1;
        if (content.startsWith("【")){
            end = content.indexOf("】");
        }else if (content.startsWith("[")){
            end = content.indexOf("]");
        }
        // 必须有签名，签名外必须有内容
        if (end > 1 && end < lastIndex && end <= SIGN_MAX_LENGTH + 1){
            return new int[]{0, end};
        }
        // 签名后置的情况
        if (content.endsWith("】")){
            start = content.lastIndexOf("【");
        }else if (content.endsWith("]")){
            start = content.lastIndexOf("[");
        }
        if (start > 0 && start < lastIndex -1 && lastIndex - start <= SIGN_MAX_LENGTH + 1){
            return  new int[]{start, lastIndex};
        }
        return null;
    }

    /**
     * 从内容中获取签名
     * 默认内容都是调整过的
     * @param content
     * @return
     */
    public static String getSignName(String content){
        int[] position = getSignPosition(content);
        if(position == null){
            return null;
        }
        return content.substring(position[0] + 1, position[1]);
    }

    /**
     * 从内容中移除的签名
     * 优先移除前置的签名，没有前置签名就移除后置的
     * @param content
     * @return
     */
    public static String removeSign(String content){
        int[] position = getSignPosition(content);
        if(position == null){
            return content;
        }
        if(position[0] == 0){
            return content.substring(position[1] + 1);
        }else{
            return content.substring(0, position[0]);
        }
    }
}
