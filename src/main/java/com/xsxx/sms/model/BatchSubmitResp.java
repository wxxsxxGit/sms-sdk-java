package com.xsxx.sms.model;

import java.util.List;

/**
 * 批量内容提交反馈
 */
@Deprecated
public class BatchSubmitResp  extends Resp{

    /**
     * status : 0
     * result : [{"msgid":"-5999107783039507381","status":0},{"msgid":"-5999107783039507380","status":0}]
     */

    private List<SubmitResp> result;

    public List<SubmitResp> getResult() {
        return result;
    }

    public void setResult(List<SubmitResp> result) {
        this.result = result;
    }
}