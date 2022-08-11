package com.xsxx.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xsxx.sms.model.BatchSubmitResp;
import com.xsxx.sms.model.Sms;
import com.xsxx.sms.model.DeliverResp;
import com.xsxx.sms.model.ReportResp;
import com.xsxx.sms.model.SubmitResp;
import com.xsxx.sms.security.MD5Util;
import com.xsxx.sms.util.SmsUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * HTTP API v2.0 提交客户端
 *
 * @author momo
 * @date 2020-01-02 13:15:15
 */
public class V2Client implements BaseApi {
    /**
     * okHttp客户端
     */
    protected OkHttpClient okHttpClient;

    /**
     * http窗口数
     */
    protected int MAX_REQUESTS_PER_HOST = 10;

    /**
     * 短信提交路径
     */
    protected String URI_SUBMIT;
    /**
     * 多内容打包短信提交路径
     */
    @Deprecated
    protected String URI_BATCHSUBMIT;
    /**
     * 状态报告路径
     */
    protected String URI_REPORT;
    /**
     * 上行报告路径
     */
    protected String URI_DELIVRD;

    /**
     * 密钥
     */
    protected String token;

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
    public V2Client(String url, String username, String token, Integer requestPerHost, String fetchURL) throws IllegalArgumentException {
        if (!SmsUtil.isURL(url) || username == null || token == null) {
            throw new IllegalArgumentException();
        }
        this.token = MD5Util.MD5(token);
        // http client
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();
        if (requestPerHost != null && requestPerHost > 5 && requestPerHost < okHttpClient.dispatcher().getMaxRequests()) {
            MAX_REQUESTS_PER_HOST = requestPerHost;
        }
        okHttpClient.dispatcher().setMaxRequestsPerHost(MAX_REQUESTS_PER_HOST);

        // URL
        if (!url.endsWith("/")) {
            url += "/";
        }
        this.URI_SUBMIT = url + "websms/smsJsonService?action=sendsms&userId=" + username;
        this.URI_BATCHSUBMIT = url + "batchwebsms/smsJsonService?userId=" + username;
        // 开启主动获取
        if (SmsUtil.isURL(fetchURL)) {
            this.URI_REPORT = fetchURL + "websms/smsJsonService?action=getsendreport&userId=" + username;
            this.URI_DELIVRD = fetchURL + "websms/smsJsonService?action=getdeliver&userId=" + username;
        }
    }

    public V2Client(String url, String username, String token, Integer requestPerHost) throws IllegalArgumentException {
        this(url, username, token, requestPerHost, null);
    }

    public V2Client(String url, String username, String token) throws IllegalArgumentException {
        this(url, username, token, null, null);
    }

    /**
     * 发送提交
     *
     * @param sms
     * @param consumer
     * @return true:同步  false 异步
     */
    @Override
    public boolean submit(Sms sms, Consumer<SubmitResp> consumer) {
        // 请求体
        Request request = makeRequest(sms);
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
                    consumer.accept(JSONObject.parseObject(response.body().string(), SubmitResp.class));
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
                    resp = JSONObject.parseObject(response.body().string(), SubmitResp.class);
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

    /**
     * 主动获取未读短信状态
     * 成功为DELIVRD，无特殊要求一次最多返回500条，可以用msgId来匹配返回的状态
     * 可提供回掉URL自动推送
     */
    @Override
    public ReportResp getReport() {
        if (URI_REPORT == null) {
            return null;
        }
        // 请求体
        Request request = makeRequest(URI_REPORT);
        // 反馈实体
        ReportResp resp = new ReportResp();
        // 同步发送
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resp = JSONObject.parseObject(response.body().string(), ReportResp.class);
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

    /**
     * 主动获取未读上行
     * 无特殊要求一次最多返回500条
     * 可提供回掉URL自动推送
     */
    @Override
    public DeliverResp getDeliver() {
        if (URI_DELIVRD == null) {
            return null;
        }
        // 请求体
        Request request = makeRequest(URI_DELIVRD);
        // 反馈实体
        DeliverResp resp = new DeliverResp();
        // 同步发送
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resp = JSONObject.parseObject(response.body().string(), DeliverResp.class);
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

    /**
     * 多内容打包同步发送
     *
     * @param smsContents 短信实体
     */
    @Override
    @Deprecated
    public BatchSubmitResp submit(List<Sms> smsContents) {
        // 请求体
        Request request = makeRequest(smsContents);
        // 反馈实体
        BatchSubmitResp resp = new BatchSubmitResp();
        // 同步发送
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                resp = JSONObject.parseObject(response.body().string(), BatchSubmitResp.class);
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

    /**
     * http请求体
     * 用于短信发送
     *
     * @param sms
     * @return
     */
    protected Request makeRequest(Sms sms) {
        // FormBody.Builder
        FormBody.Builder builder = new FormBody.Builder()
                .add("md5password", token)
                .add("content", sms.getContent())
                .add("mobile", sms.getMobile());
        // 扩展码
        if (sms.getExtCode() != null) {
            builder.add("extCode", sms.getExtCode());
        }
        // 自定义msgId
        if (sms.getMsgId() != null) {
            builder.add("msgId", sms.getMsgId().toString());
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(URI_SUBMIT)
                .post(requestBody)
                .build();
    }

    /**
     * http请求体
     * 用于多内容打包短信发送
     *
     * @param smsContents
     * @return
     */
    @Deprecated
    protected Request makeRequest(List<Sms> smsContents) {
        FormBody.Builder builder = new FormBody.Builder()
                .add("md5password", token)
                .add("contentArr", JSON.toJSONString(smsContents));
        RequestBody requestBody = builder.build();
        builder.build();
        return new Request.Builder()
                .url(URI_BATCHSUBMIT)
                .post(requestBody)
                .build();
    }

    /**
     * 短信体
     * 用于非发送接口
     *
     * @param url
     * @return
     */
    @Deprecated
    protected Request makeRequest(String url) {
        // FormBody.Builder
        FormBody.Builder builder = new FormBody.Builder()
                .add("md5password", token);
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }
}
