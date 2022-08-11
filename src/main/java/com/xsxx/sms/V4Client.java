package com.xsxx.sms;

import com.alibaba.fastjson.JSONObject;
import com.xsxx.sms.model.Sms;
import com.xsxx.sms.security.Hmac;
import com.xsxx.sms.util.SmsUtil;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.List;

/**
 * HTTP API v4.0 提交客户端
 * 四代接口增加：
 * 1. 加密验证
 * 2. 防重放
 * 3. 推送验证
 *
 * @author momo
 * @date 2020-01-02 13:15:15
 */
public class V4Client extends V2Client {

    /**
     * 初始化
     *
     * @param url            短信提交地址
     * @param username       用户名
     * @param token          密码
     * @param requestPerHost http窗口数量
     * @param fetchURL       获取状态/上行报告地址
     * @throws IllegalArgumentException
     */
    public V4Client(String url, String username, String token, Integer requestPerHost, String fetchURL) throws IllegalArgumentException {
        super(url, username, token, requestPerHost, fetchURL);
        this.token = token;
        this.URI_SUBMIT = url + "sms/send/" + username;
        this.URI_BATCHSUBMIT = url + "sms/sendBatch/" + username;
        // 开启主动获取
        if (SmsUtil.isURL(fetchURL)) {
            this.URI_REPORT = fetchURL + "sms/getReport/" + username;
            this.URI_DELIVRD = fetchURL + "sms/getUpstream/" + username;
        }
    }

    public V4Client(String url, String username, String token, Integer requestPerHost) throws IllegalArgumentException {
        this(url, username, token, requestPerHost, null);
    }

    public V4Client(String url, String username, String token) throws IllegalArgumentException {
        this(url, username, token, null, null);
    }

    /**
     * 建立请求体
     *
     * @param url
     * @param body
     * @return
     */
    private Request makeRequest(String url, String body) {
        long timeStamp = System.currentTimeMillis();
        String signature = Hmac.createSignature(timeStamp, body, this.token);
        String header = "HMAC-SHA256 " + timeStamp + "," + signature;
        MediaType mediaType = MediaType.Companion.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.Companion.create(body, mediaType);
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization", header)
                .post(requestBody)
                .build();
    }

    /**
     * http请求体
     * 用于短信发送
     *
     * @param sms
     * @return
     */
    @Override
    protected Request makeRequest(Sms sms) {
        String body = JSONObject.toJSONString(sms);
        return makeRequest(URI_SUBMIT, body);
    }

    /**
     * http请求体
     * 用于多内容打包短信发送
     *
     * @param smsContents
     * @return
     */
    @Override
    @Deprecated
    protected Request makeRequest(List<Sms> smsContents) {
        String body = JSONObject.toJSONString(smsContents);
        return makeRequest(URI_BATCHSUBMIT, body);
    }

    /**
     * 一次获取状态报告的数量，用于主动获取接口
     */
    @Deprecated
    private int maxSize;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * http请求体
     * 用于非发送接口
     *
     * @param url
     * @return
     */
    @Override
    protected Request makeRequest(String url) {
        String body = maxSize > 0 ? "{\"maxSize\":" + maxSize + "}" : "";
        return makeRequest(url, body);
    }
}
