package com.xsxx.sms;

import cn.hutool.json.JSONUtil;
import com.xsxx.sms.model.BalanceResp;
import com.xsxx.sms.model.DailyStatsResp;
import com.xsxx.sms.model.Sms;
import com.xsxx.sms.model.SubmitResp;
import com.xsxx.sms.security.Hmac;
import com.xsxx.sms.util.AesUtil;
import com.xsxx.sms.util.DateUtils;
import com.xsxx.sms.util.SmsUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * HTTP API v4.0 提交客户端
 * 四代接口增加：
 * 1. 加密验证
 * 2. 防重放
 * 3. 推送验证
 * 接口文档地址： https://api-wiki.wxxsxx.com
 *
 * @author viveguan
 * @date 2022/08/12
 */
public class V4Client extends V2Client {
    private String URI_AES_SUBMIT;

    /**
     * 初始化
     *
     * @param url            短信接口提交地址
     * @param spId           我方提供的发送账号的唯一标识
     * @param spKey          我方提供的发送账号的预共享密钥的
     * @param requestPerHost http窗口数量
     * @param fetchURL       获取状态/上行报告地址
     * @throws IllegalArgumentException
     */
    public V4Client(String url, String spId, String spKey, Integer requestPerHost, String fetchURL) throws IllegalArgumentException {
        super(url, spId, spKey, requestPerHost, fetchURL);
        if (!url.endsWith("/")) {
            url += "/";
        }
        this.token = spKey;
        this.URI_SUBMIT = url + "sms/send/" + spId;
        this.URI_AES_SUBMIT = url + "sms/secureSend/" + spId;
        this.URI_BATCHSUBMIT = url + "sms/sendBatch/" + spId;
        // 开启主动获取
        if (SmsUtil.isURL(fetchURL)) {
            if (!fetchURL.endsWith("/")) {
                fetchURL += "/";
            }
            this.URI_REPORT = fetchURL + "sms/getReport/" + spId;
            this.URI_DELIVRD = fetchURL + "sms/getUpstream/" + spId;
            this.URI_BALANCE = fetchURL + "sms/getBalance/" + spId;
            this.URI_DAILY_STATS = fetchURL + "sms/getDailyStats/" + spId;
        }
    }

    public V4Client(String url, String spId, String spKey, Integer requestPerHost) throws IllegalArgumentException {
        this(url, spId, spKey, requestPerHost, null);
    }

    public V4Client(String url, String spId, String spKey) throws IllegalArgumentException {
        this(url, spId, spKey, null, null);
    }

    /**
     * 主动获取预付费发送账号余额
     * 预付费账号剩余余额查询。
     */
    @Override
    public BalanceResp getBalance() {
        if (URI_BALANCE == null) {
            return null;
        }
        // 请求体
        Request request = makeRequest(URI_BALANCE, null);
        // 反馈实体
        BalanceResp resp = new BalanceResp();
        // 同步发送
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resp = JSONUtil.toBean(response.body().string(), BalanceResp.class);
            } else {
                resp.setStatus(response.code());
                resp.setMsg(response.message());
            }
        } catch (IOException e) {
            resp.setStatus(-1);
            resp.setMsg(e.getMessage());
        }
        return resp;
    }

    @Override
    public DailyStatsResp getDailyStats(String date) {
        if (date == null || URI_DAILY_STATS == null) {
            return null;
        }
        // 反馈实体
        DailyStatsResp resp = new DailyStatsResp();
        try {
            Date checkDate = DateUtils.parse(date, DateUtils.PATTERNDATE);
            if (checkDate == null) {
                resp.setStatus(-1);
                resp.setMsg("date pattern incorrect format (yyyyMMdd)");
                return resp;
            }
            // 请求体
            Request request = makeRequest(URI_DAILY_STATS, "{\"date\":" + date + "}");
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resp = JSONUtil.toBean(response.body().string(), DailyStatsResp.class);
            } else {
                resp.setStatus(response.code());
                resp.setMsg(response.message());
            }
        } catch (Exception e) {
            resp.setStatus(-1);
            resp.setMsg(e.getMessage());
        }
        return resp;
    }


    /**
     * 建立请求体
     *
     * @param url
     * @param body
     * @return
     */
    private Request makeRequest(String url, String body) {
        if (body == null) {
            body = "";
        }
        long timeStamp = System.currentTimeMillis();
        String signature = Hmac.createSignature(timeStamp, body, this.token);
        String header = "HMAC-SHA256 " + timeStamp + "," + signature;
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, body);
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
        String body = JSONUtil.toJsonStr(sms);
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
        String body = JSONUtil.toJsonStr(smsContents);
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

    /**
     * AES加密
     * 发送提交
     *
     * @param sms
     * @param consumer
     * @return true:同步  false 异步
     */
    public boolean submitByAes(Sms sms, Consumer<SubmitResp> consumer) throws Exception {

        // 请求体
        String body = JSONUtil.toJsonStr(sms);
        Map<String, String> aesBody = new HashMap<>();
        aesBody.put("content", AesUtil.aesEncode(token, body));
        Request request = makeRequest(URI_AES_SUBMIT, JSONUtil.toJsonStr(aesBody));
        // 发送 — 平缓时使用同步方法， 任务数 > 线程数*2 时，使用同步减速
        if (okHttpClient.dispatcher().queuedCallsCount() < MAX_REQUESTS_PER_HOST) {
            // 异步
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SubmitResp submitResp = new SubmitResp();
                    submitResp.setStatus(-1);
                    submitResp.setMsg(e.getMessage());
                    consumer.accept(submitResp);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    consumer.accept(JSONUtil.toBean(response.body().string(), SubmitResp.class));
                    response.body().close();
                }
            });
            return false;
        } else {
            // 反馈实体
            SubmitResp resp = new SubmitResp();
            // 同步发送
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    resp = JSONUtil.toBean(response.body().string(), SubmitResp.class);
                } else {
                    resp.setStatus(response.code());
                    resp.setMsg(response.message());
                }
            } catch (IOException e) {
                resp.setStatus(-1);
                resp.setMsg(e.getMessage());
            }
            consumer.accept(resp);
            return true;
        }
    }
}
