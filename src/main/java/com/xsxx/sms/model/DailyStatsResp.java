package com.xsxx.sms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 日统计查询响应
 * {
 * "status" : 0,                             // 请求成功
 * "result":
 * {
 * "spId" : "apiSendUser01",         // 发送账号
 * "date" : 20200101,               // 查询的日期
 * "sendCount" : 1000,               // 提交条数
 * "feeCount" : 1500 ,       // 发送条数
 * "successCount" : 1400,// 成功条数
 * "failCount" : 80,      // 失败条数
 * "exceptionCount" : 0,      // 异常条数
 * "noRespCount" : 20,      // 未反馈条数
 * }
 * }
 *
 * @author guanh
 * @date 2022/8/12 13:28
 */
public class DailyStatsResp extends Resp {

    /**
     *
     */
    private List<DailyStats> result = new ArrayList<>();

    public List<DailyStats> getResult() {
        return result;
    }

    public void setResult(List<DailyStats> result) {
        this.result = result;
    }

}
