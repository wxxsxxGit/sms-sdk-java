package com.xsxx.sms.model.template;

/**
 * 添加模板
 *
 * @author guanh
 * @date 2022/9/15 17:43
 */
public class SmsTemplate {

    /**
     * 模板编号 唯一
     */
    private Long templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型
     *
     * @see TemplateTypeEnum#ordinal()
     */
    private int templateType;
    /**
     * 模板内容，支持变量
     * eg.  	您正在申请线上线下短信报备模板，验证码为：${code}，5分钟内有效！
     */
    private String templateContent;

    /**
     * 模板备注信息
     */
    private String remark;

    public Long getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(Long templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
