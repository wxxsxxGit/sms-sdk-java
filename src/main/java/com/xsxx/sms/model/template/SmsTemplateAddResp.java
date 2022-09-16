package com.xsxx.sms.model.template;

import com.xsxx.sms.model.Resp;

/**
 * 报备模板、修改模板响应参数
 * @author guanh 
 * @date 2022/9/15 17:43
 */
public class SmsTemplateAddResp extends Resp {
    /**
     * 模板编号
     */
    private Long templateCode;

    public Long getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(Long templateCode) {
        this.templateCode = templateCode;
    }
}
