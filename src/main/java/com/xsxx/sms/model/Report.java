package com.xsxx.sms.model;

/**
 * 状态报告
 *
 * @author momo
 * @date 2019-12-09 13:55:43
 * <p>
 * phone : 18262276782
 * msgId : -6004182853835414462
 * status : PHONERR
 * sendTime : 20171025111347
 * doneTime : 20171025111347
 */
public class Report {
    /**
     * 短信手机号
     */
    private String phone;
    /**
     * 短信msgId，与短信submitResp中的MsgId 一致
     */
    private String msgid;
    /**
     * 状态码 DELIVRD 是发送成功
     */
    private String status;
    /**
     * 手机收到短信时间
     */
    private String donetime;
    /**
     * 短信发送时间
     */
    private String submittime;
    /**
     * 手机上显示的码号
     */
    private String spno;
    /**
     * V4接口自定义字段，最大64字符
     */
    private String sId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getDonetime() {
        return donetime;
    }

    public void setDonetime(String donetime) {
        this.donetime = donetime;
    }

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }

    public String getSpno() {
        return spno;
    }

    public void setSpno(String spno) {
        this.spno = spno;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }
}
