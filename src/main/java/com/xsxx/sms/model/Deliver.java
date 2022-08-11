package com.xsxx.sms.model;

/**
 * 上行
 *
 * @author momo
 * @date 2020-01-03 17:32:35
 * <p>
 * phone : 18262276782
 * extCode : -6004182853835414462
 * content : PHONERR
 * {
 * "status":0,
 * "result":[
 * {"phone":"13314599201","extCode":"","content":"td","receivetime":"20180827042439"}
 * ]
 * }
 */
public class Deliver {

    /**
     * 手机号
     */
    private String phone;
    /**
     * 通道的拓展码
     */
    private String extCode;
    /**
     * 用户回的信息的内容
     */
    private String content;
    /**
     * 上行收到时间
     */
    private String receivetime;

    /**
     * 真实发送通道号
     */
    private String spno;

    /**
     * V4接口，短信msgId，与短信submitResp中的MsgId 一致
     */
    private String msgid;
    /**
     * V4接口，自定义字段，最大64字符
     */
    private String sId;


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

    public String getSpno() {
        return spno;
    }

    public void setSpno(String spno) {
        this.spno = spno;
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
}
