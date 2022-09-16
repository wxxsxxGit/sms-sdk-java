package com.xsxx.sms.model.template;

public enum TemplateAuditStatusEnum {
    WAITING("待审核"),
    PASS("审核通过"),
    REJECT("审核拒绝"),
    DISABLE("模板禁用"),
    DELETED("已删除");
    private String stautsDesc;

    TemplateAuditStatusEnum(String stautsDesc) {
        this.stautsDesc = stautsDesc;
    }

    public String getStautsDesc() {
        return stautsDesc;
    }
}