package com.xsxx.sms.model;

/**
 * 提交反馈
 *
 * @author momo
 * @date 18-8-30 下午8:17
 */
public class SubmitResp extends Resp {

    /**
     * 提交反馈
     */
    private Long msgid;

    public Long getMsgid() {
        return msgid;
    }

    public void setMsgid(Long msgid) {
        this.msgid = msgid;
    }
}
