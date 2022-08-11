package com.xsxx.sms.constants;

/**
 * 1-12 反name
 * key不能超过7个字符
 *
 * @author momo
 * @date 2019-07-05 22:41:40
 */
public enum ApiStatus {
    // 主动获取状态报告路径
    REPORT_URI_NOT_CONFIG(-11, "no config found: report url");

    /**
     * 错误码号
     */
    private int status;

    /**
     * 错误信息
     */
    private String msg;

    ApiStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
