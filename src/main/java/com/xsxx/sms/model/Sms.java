package com.xsxx.sms.model;

/**
 * 短信实体
 * 最多100个号码，每个手机号保证是11位数字有效手机号
 * Sms sms = new Sms("19900000000","【签名】不带扩展码号");
 * Sms sms = new Sms("19900000000,199000000001","【签名】带扩展码号","666");
 * Sms sms = new Sms("19900000000,199000000001","【签名】带扩展码号,自定义msgId","666",123456789L); （V2接口实现自定义字段，仅支持Long型）
 * Sms sms = new Sms("19900000000,199000000001","【签名】带扩展码号,自定义msgId","666","customMsgId_without_emoji");（V4接口实现自定义字段）
 *
 * @author momo
 * @date 2020-01-02 22:12:40
 */
public class Sms {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 短信内容
     * 必须以【签名20字以内】开头，保证服务端可以正确获取到签名
     */
    private String content;

    /**
     * 通道的拓展码
     */
    private String extCode;

    /**
     * 短信msgId，可选 ,必须为8字节长整型，否则反错。默认不传此值，由我们
     * 平台生成一个msgId返回；如设置此值，平台将使用此msgId作为此次提
     * 交的唯一编号并返回此msgId
     */
    private Long msgId;

    /**
     * 客户自定义(V4接口支持)
     * 最大长度24，不支持表情符
     */
    private String sId;

    /**
     * construct
     *
     * @param mobile
     * @param content
     */
    public Sms(String mobile, String content) {
        this.mobile = mobile;
        this.content = content;
    }

    public Sms(String mobile, String content, String extCode) {
        this.mobile = mobile;
        this.content = content;
        this.extCode = extCode;
    }

    /**
     * 二代接口自定义msgId
     * @param mobile
     * @param content
     * @param extCode
     * @param msgId
     */
    public Sms(String mobile, String content, String extCode, Long msgId) {
        this.mobile = mobile;
        this.content = content;
        this.extCode = extCode;
        this.msgId = msgId;
    }

    /**
     * 四代接口自定义sId
     * @param mobile
     * @param content
     * @param extCode
     * @param sId
     */
    public Sms(String mobile, String content, String extCode, String sId) {
        this.mobile = mobile;
        this.content = content;
        this.extCode = extCode;
        this.sId = sId;
    }

    public Sms(String mobile, String content, Long msgId) {
        this.mobile = mobile;
        this.content = content;
        this.msgId = msgId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtCode() {
        return extCode;
    }

    public void setExtCode(String extCode) {
        this.extCode = extCode;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }
}
