package com.xsxx.sms.model.template;

public enum TemplateTypeEnum {
    /**
     * 模板类型
     */
    VERIFICATION_CODE("验证码"),
    NOTIFICATION("通知类"),
    MARKETING("营销类");
    private String desc;

    TemplateTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

    