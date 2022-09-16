package com.xsxx.sms.model.template;

import com.xsxx.sms.model.Resp;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量短信发送 响应数据
 *
 * @author guanh
 * @date 2022/9/16 10:24
 */
public class SmsBatchSubimtByTemplateResp extends Resp {

    /**
     * 短信下发结果信息列表
     */
    private List<SubmitByTemplateRespVo> result = new ArrayList<>();

    /**
     * 批量短信发送 响应数据 单条VO
     */
    public static class SubmitByTemplateRespVo extends Resp {

        /**
         * 单个手机号码
         * 11位标准国内手机号，格式化去掉86，+86等国家码信息
         */
        private String mobile;
        /**
         * 单个号码下发短信实际拆分条数
         */
        private int splitCount;
        /**
         * 自定义msgid
         */
        private String msgid;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

    public List<SubmitByTemplateRespVo> getResult() {
        return result;
    }

    public void setResult(List<SubmitByTemplateRespVo> result) {
        this.result = result;
    }
}
