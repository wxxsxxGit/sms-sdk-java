package com.xsxx.sms.model;

/**
 * 日统计信息
 *
 * @author guanh
 * @date 2022/8/12 13:15
 */
public class DailyStats {
    /**
     * 发送账号
     */
    private String spId;
    /**
     * 查询的日期
     * 格式：yyyyMMdd
     */
    private String date;

    /**
     * 提交的客户手机号码数
     */
    private long sendCount;
    /**
     * 短信发送运营商网关短信分片数（超过70字按照67字拆分）
     * eg.提交短信内容为 71字  sendCount = 1 feeCount = 2
     */
    private long feeCount;

    /**
     * 运营商网关成功下发短信分片数
     */
    private long successCount;

    /**
     * 运营商网关未触达下发短信分片数
     */
    private long failCount;
    /**
     * 未正确提交至运营商的短信分片数
     */
    private long exceptionCount;
    /**
     * 运营商未反馈短信分片状态数量
     */
    private long noRespCount;

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getSendCount() {
        return sendCount;
    }

    public void setSendCount(long sendCount) {
        this.sendCount = sendCount;
    }

    public long getFeeCount() {
        return feeCount;
    }

    public void setFeeCount(long feeCount) {
        this.feeCount = feeCount;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public long getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(long exceptionCount) {
        this.exceptionCount = exceptionCount;
    }

    public long getNoRespCount() {
        return noRespCount;
    }

    public void setNoRespCount(long noRespCount) {
        this.noRespCount = noRespCount;
    }
}
