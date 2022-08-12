package com.xsxx.sms.model;

/**
 * 状态报告
 *
 * @author momo
 * @date 2019-12-09 13:55:43
 * <p>
 * {
 * "phone" : "13921350591",             // 手机号码
 * "msgid" : "-8629637681836384963",    // 与提交中的msgid 一致
 * "status" : "DELIVRD" ,               // 短信状态，参考附表2
 * "donetime" : "20170816153922",       // 短信到达时间
 * "fee" : 2,                           // 短信计费条数
 * "sId" : "123456789abcdefg"           // 批次号
 * }
 */
public class Report {
    /**
     * 短信手机号
     */
    private String phone;
    /**
     * 短信msgId，
     * 与短信submitResp中的MsgId 一致
     */
    private String msgid;
    /**
     * 状态码
     * DELIVRD 发送成功，其他均为失败
     */
    private String status;
    /**
     * 手机收到短信时间
     */
    private String donetime;

    /**
     * 短信计费条数
     * V4接口适用
     */
    private int fee;
    /**
     * 自定义 批次号
     * V4接口适用
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

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }
}
