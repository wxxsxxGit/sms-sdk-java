package com.xsxx.sms;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xsxx.sms.model.*;
import com.xsxx.sms.model.template.*;
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
    /**
     * aes128 加密发送
     */
    private String URI_AES_SUBMIT;
    /**
     * 添加模板
     */
    private String URI_ADD_TEMPLATE;
    /**
     * 修改模板
     */
    private String URI_MODIFY_TEMPLATE;
    /**
     * 删除模板
     */
    private String URI_DEL_TEMPLATE;
    /**
     * 查询模板状态
     */
    private String URI_QUERY_TEMPLATE_STATUS;
    /**
     * 模板短信发送
     */
    private String URI_SUBMIT_BY_TEMPLATE;
    /**
     * 批量模板短信发送
     */
    private String URI_BATCH_SUBMIT_BY_TEMPLATE;

    /**
     * 初始化
     *
     * @param url            短信接口提交地址
     * @param spId           我方提供的发送账号的唯一标识
     * @param spKey          我方提供的发送账号的预共享密钥的
     * @param requestPerHost http窗口数量
     * @param fetchURL       获取状态/上行报告地址
     * @param templateURL    模板报备发送提交url
     * @throws IllegalArgumentException
     */
    public V4Client(String url, String spId, String spKey, Integer requestPerHost, String fetchURL, String templateURL) throws IllegalArgumentException {
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
        if (SmsUtil.isURL(templateURL)) {
            if (!templateURL.endsWith("/")) {
                templateURL += "/";
            }
            this.URI_ADD_TEMPLATE = templateURL + "sms/template/add/" + spId;
            this.URI_MODIFY_TEMPLATE = templateURL + "sms/template/modify/" + spId;
            this.URI_DEL_TEMPLATE = templateURL + "sms/template/delete/" + spId;
            this.URI_QUERY_TEMPLATE_STATUS = templateURL + "sms/template/status/" + spId;
            this.URI_SUBMIT_BY_TEMPLATE = templateURL + "sms/template/sendSms/" + spId;
            this.URI_BATCH_SUBMIT_BY_TEMPLATE = templateURL + "sms/template/sendBatchSms/" + spId;
        }
    }

    public V4Client(String url, String spId, String spKey, Integer requestPerHost) throws IllegalArgumentException {
        this(url, spId, spKey, requestPerHost, null, null);
    }

    public V4Client(String url, String spId, String spKey) throws IllegalArgumentException {
        this(url, spId, spKey, null, null, null);
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

    /**
     * 添加或修改模板
     *
     * @param isAdd       true 添加模板 false 修改模板
     * @param smsTemplate
     * @param consumer
     * @return true:同步  false 异步
     */
    public boolean addOrModifyTemplate(boolean isAdd, SmsTemplate smsTemplate, Consumer<SmsTemplateAddResp> consumer) throws Exception {

        // 请求体
        Request request = makeRequest(isAdd ? URI_ADD_TEMPLATE : URI_MODIFY_TEMPLATE, JSONUtil.toJsonStr(smsTemplate));
        // 发送 — 平缓时使用同步方法， 任务数 > 线程数*2 时，使用同步减速
        if (okHttpClient.dispatcher().queuedCallsCount() < MAX_REQUESTS_PER_HOST) {
            // 异步
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SmsTemplateAddResp submitResp = new SmsTemplateAddResp();
                    submitResp.setStatus(-1);
                    submitResp.setMsg(e.getMessage());
                    consumer.accept(submitResp);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    consumer.accept(JSONUtil.toBean(response.body().string(), SmsTemplateAddResp.class));
                    response.body().close();
                }
            });
            return false;
        } else {
            // 反馈实体
            SmsTemplateAddResp resp = new SmsTemplateAddResp();
            // 同步发送
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    resp = JSONUtil.toBean(response.body().string(), SmsTemplateAddResp.class);
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
     * 删除模板
     *
     * @param templateCode 模板编号
     * @param consumer
     * @return true:同步  false 异步
     */
    public boolean deleteTemplate(Long templateCode, Consumer<Resp> consumer) throws Exception {

        // 请求体
        Map<String, Long> body = new HashMap<>();
        body.put("templateCode", templateCode);
//        System.out.println("delete params:\n"+JSONUtil.toJsonStr(body));
        Request request = makeRequest(URI_DEL_TEMPLATE, JSONUtil.toJsonStr(body));
        // 发送 — 平缓时使用同步方法， 任务数 > 线程数*2 时，使用同步减速
        if (okHttpClient.dispatcher().queuedCallsCount() < MAX_REQUESTS_PER_HOST) {
            // 异步
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Resp resp = new Resp();
                    resp.setStatus(-1);
                    resp.setMsg(e.getMessage());
                    consumer.accept(resp);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    consumer.accept(JSONUtil.toBean(response.body().string(), Resp.class));
                    response.body().close();
                }
            });
            return false;
        } else {
            // 反馈实体
            Resp resp = new Resp();
            // 同步发送
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    resp = JSONUtil.toBean(response.body().string(), Resp.class);
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
     * 查询模板状态
     *
     * @param templateCodes 短信模板唯一编号列表 一批次最多100
     * @param consumer
     * @return true:同步  false 异步
     */
    public boolean queryTemplateStatus(List<Long> templateCodes, Consumer<SmsTemplateQueryStatusResp> consumer) throws Exception {

        // 请求体
        if (CollUtil.isEmpty(templateCodes)) {
            throw new Exception("短信模板唯一编号列表为空");
        }
        if (templateCodes.size() > 100) {
            throw new Exception("短信模板唯一编号列表一批次最多100");
        }
        Map<String, String> body = new HashMap<>();
        body.put("templateCodes", CollUtil.join(templateCodes, StrUtil.COMMA));
//        System.out.println("query params:\n"+JSONUtil.toJsonStr(body));
        Request request = makeRequest(URI_QUERY_TEMPLATE_STATUS, JSONUtil.toJsonStr(body));
        // 发送 — 平缓时使用同步方法， 任务数 > 线程数*2 时，使用同步减速
        if (okHttpClient.dispatcher().queuedCallsCount() < MAX_REQUESTS_PER_HOST) {
            // 异步
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SmsTemplateQueryStatusResp submitResp = new SmsTemplateQueryStatusResp();
                    submitResp.setStatus(-1);
                    submitResp.setMsg(e.getMessage());
                    consumer.accept(submitResp);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    consumer.accept(JSONUtil.toBean(response.body().string(), SmsTemplateQueryStatusResp.class));
                    response.body().close();
                }
            });
            return false;
        } else {
            // 反馈实体
            SmsTemplateQueryStatusResp resp = new SmsTemplateQueryStatusResp();
            // 同步发送
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    resp = JSONUtil.toBean(response.body().string(), SmsTemplateQueryStatusResp.class);
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
     * 单个模板短信发送
     *
     * @param smsSubimtByTemplateReqVo 模板短信发送 请求参数
     * @param consumer
     * @return true:同步  false 异步
     */
    public boolean submitByTemplateCode(SmsSubimtByTemplateReqVo smsSubimtByTemplateReqVo, Consumer<SmsSubimtByTemplateResp> consumer) throws Exception {

        // 请求体
        Request request = makeRequest(URI_SUBMIT_BY_TEMPLATE, JSONUtil.toJsonStr(smsSubimtByTemplateReqVo));
        // 发送 — 平缓时使用同步方法， 任务数 > 线程数*2 时，使用同步减速
        if (okHttpClient.dispatcher().queuedCallsCount() < MAX_REQUESTS_PER_HOST) {
            // 异步
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SmsSubimtByTemplateResp submitResp = new SmsSubimtByTemplateResp();
                    submitResp.setStatus(-1);
                    submitResp.setMsg(e.getMessage());
                    consumer.accept(submitResp);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    consumer.accept(JSONUtil.toBean(response.body().string(), SmsSubimtByTemplateResp.class));
                    response.body().close();
                }
            });
            return false;
        } else {
            // 反馈实体
            SmsSubimtByTemplateResp resp = new SmsSubimtByTemplateResp();
            // 同步发送
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    resp = JSONUtil.toBean(response.body().string(), SmsSubimtByTemplateResp.class);
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
     * 批量模板短信发送
     *
     * @param smsSubimtByTemplateReqVoList 模板短信发送 请求参数
     * @param consumer
     * @return true:同步  false 异步
     */
    public boolean batchSubmitByTemplateCode(List<SmsSubimtByTemplateReqVo> smsSubimtByTemplateReqVoList, Consumer<SmsBatchSubimtByTemplateResp> consumer) throws Exception {

        // 请求体
        Request request = makeRequest(URI_BATCH_SUBMIT_BY_TEMPLATE, JSONUtil.toJsonStr(smsSubimtByTemplateReqVoList));
        // 发送 — 平缓时使用同步方法， 任务数 > 线程数*2 时，使用同步减速
        if (okHttpClient.dispatcher().queuedCallsCount() < MAX_REQUESTS_PER_HOST) {
            // 异步
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SmsBatchSubimtByTemplateResp submitResp = new SmsBatchSubimtByTemplateResp();
                    submitResp.setStatus(-1);
                    submitResp.setMsg(e.getMessage());
                    consumer.accept(submitResp);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    consumer.accept(JSONUtil.toBean(response.body().string(), SmsBatchSubimtByTemplateResp.class));
                    response.body().close();
                }
            });
            return false;
        } else {
            // 反馈实体
            SmsBatchSubimtByTemplateResp resp = new SmsBatchSubimtByTemplateResp();
            // 同步发送
            try {
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    resp = JSONUtil.toBean(response.body().string(), SmsBatchSubimtByTemplateResp.class);
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
