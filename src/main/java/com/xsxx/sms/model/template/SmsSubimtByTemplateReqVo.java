package com.xsxx.sms.model.template;

/**
 * 模板短信发送 请求参数
 * @author guanh 
 * @date 2022/9/16 10:32
 */
public class SmsSubimtByTemplateReqVo {
    /**
     * 短信签名名称
     */
    private String signName;
    /**
     * 短信模板唯一编号,
     */
    private long templateCode;
    /**
     * 短信模板变量对应的实际值
     * json字符串规范
     */
    private String params;
    /**
     * 短信号码，多个用“,”分割，号码数量<=10000
     */
    private String mobile;
    /**
     * 自定义msgid
     */
    private String msgid;
    /**
     * 扩展码，必须可解析为数字
     */
    private String extCode;
    /**
     * 批次号
     */
    private String sId;

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public long getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(long templateCode) {
        this.templateCode = templateCode;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(String extCode) {
        this.extCode = extCode;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }
}
