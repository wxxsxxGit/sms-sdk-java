package com.xsxx.sms.model;

/**
 * 上行
 *
 * @author momo
 * @date 2020-01-03 17:32:35
 * <p>
 * {
 * "status" : 0,                             // 固定值0
 * "result":
 * [
 * <p>
 * {
 * "phone" : "13921350591",         // 手机号码
 * "extCode" : "682",               // 用户提交短信时候带的extCode
 * "content" : "上行回复内容" ,       // 上行内容
 * "receivetime" : "20170816153922",// 短信到达时间
 * "sId" : "123456789abcdefg",      // 批次号
 * "sign" : "签名",
 * "msgid" : "-8629637681836384963" // 短信唯一编号
 * }
 * // 这里是数组会有多条，默认最大500条
 * ，平台可配置
 * ]
 * }
 */
public class Deliver {

    /**
     * 用户回复的手机号码
     */
    private String phone;
    /**
     * 用户提交短信时候带的extCode
     */
    private String extCode;
    /**
     * 用户回复的信息内容
     */
    private String content;
    /**
     * 运营商推送的用户回复信息时间
     * 时间格式化： yyyyMMddHHmmss
     */
    private String receivetime;

    /**
     * 对应短信提交时的msgid
     */
    private String msgid;
    /**
     * V4接口，对应短信提交时的sId
     */
    private String sId;

    /**
     * 对应短信提交时的签名（无下发上行没有此字段）
     */
    private String sign;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime;
    }

    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(String extCode) {
        this.extCode = extCode;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
