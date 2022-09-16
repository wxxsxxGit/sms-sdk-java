package com.xsxx.sms.model.template;

import com.xsxx.sms.model.Resp;

/**
 * 模板短信发送 响应数据
 *
 * @author guanh
 * @date 2022/9/16 10:24
 */
public class SmsSubimtByTemplateResp extends Resp {
    /**
     * 错误手机号码列表，多个用“,”分割
     */
    private String failList;
    /**
     * 成功提交手机号码列表，多个用“,”分割
     * 11位标准国内手机号，格式化去掉86，+86等国家码信息
     */
    private String successList;
    /**
     * 单个号码下发短信实际拆分条数
     */
    private int splitCount;
    /**
     * 自定义msgid
     */
    private String msgid;

    public String getFailList() {
        return failList;
    }

    public void setFailList(String failList) {
        this.failList = failList;
    }

    public String getSuccessList() {
        return successList;
    }

    public void setSuccessList(String successList) {
        this.successList = successList;
    }

    public int getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(int splitCount) {
        this.splitCount = splitCount;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}
