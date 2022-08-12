package com.xsxx.sms.model;

/**
 * 账号余额查询响应
 * {
 * 	"status": 0       // 请求成功
 * 	"result": 10000    //当前余额条数
 * }
 *
 * @author guanh
 * @date 2022/8/12 13:15
 */
public class BalanceResp extends Resp {
    /**
     * 当前余额条数
     */
    private int result;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
