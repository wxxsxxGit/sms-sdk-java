package com.xsxx.sms.model.template;

import java.util.List;

/**
 * 添加模板
 *
 * @author guanh
 * @date 2022/9/15 17:43
 */
public class SmsTemplate {

    public static class PicFileItem {
        /**
         * 涉及到签名资质证明材料，采用图片经base64编码
         * 后的字符串,图片不超过2 MB
         */
        private String contents;
        /**
         * 仅支持可选后缀值：jpg,png,jpeg 。文件后缀名不带.
         */
        private String suffix;

        public PicFileItem(String contents, String suffix) {
            this.contents = contents;
            this.suffix = suffix;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }
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
    private Integer templateType;
    /**
     * 模板内容，支持变量
     * eg.  	您正在申请线上线下短信报备模板，验证码为：${code}，5分钟内有效！
     */
    private String templateContent;

    /**
     * 模板备注信息
     */
    private String remark;

    /**
     * 文件base64字符串信息
     */
    private List<PicFileItem> picFileList;
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

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
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

    public List<PicFileItem> getPicFileList() {
        return picFileList;
    }

    public void setPicFileList(List<PicFileItem> picFileList) {
        this.picFileList = picFileList;
    }
}
