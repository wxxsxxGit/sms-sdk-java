package com.xsxx.sms.model.template;

import com.xsxx.sms.model.Resp;

import java.util.List;

/**
 * 查询模板状态响应VO
 */
public class SmsTemplateQueryStatusResp extends Resp {
    /**
     * 短信模板对象信息列表
     */
    private List<TemplateStatusVo> templateList;

    /**
     * 查询模板状态 响应VO
     */
    public static class TemplateStatusVo extends SmsTemplate {
        /**
         * 模板编号查询状态
         */
        private int status;
        /**
         * 提示信息 默认null
         */
        private String msg;
        /**
         * 模板审核状态。取值：
         * 0：审核中。
         * 1：审核通过。
         * 2：审核失败
         * 3:   模板禁用
         */
        private int auditStatus;

        /**
         * 审核备注。
         * 如果审核状态为
         * [2：审核失败]
         * [3：模板禁用] 此处显示审核的具体原因。
         */
        private String auditReason;
        /**
         * 模板申请创建时间
         * 格式：yyyy-MM-dd HH:mm:ss
         */
        private String createTime;

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public String getAuditReason() {
            return auditReason;
        }

        public void setAuditReason(String auditReason) {
            this.auditReason = auditReason;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public List<TemplateStatusVo> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<TemplateStatusVo> templateList) {
        this.templateList = templateList;
    }
}
