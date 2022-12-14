package com.xsxx.sms;

import com.xsxx.sms.model.*;

import java.util.List;
import java.util.function.Consumer;

/**
 * HTTP API
 *
 * @author momo
 * @date 2020-01-02 13:15:15
 */
public interface BaseApi {

    /**
     * http窗口数
     */
    int MAX_REQUESTS_PER_HOST = 10;

    /**
     * 批量接口最大打包条数
     */
    int MAX_BATCH_CONTENT = 100;

    /**
     * 单内容多号码接口最大号码数量
     */
    int MAX_BATCH_PHONE = 100;

    /**
     * 发送
     *
     * @param sms
     * @param consumer
     * @return 发送队列数量
     */
    boolean submit(Sms sms, Consumer<SubmitResp> consumer);

    /**
     * 同步批量发送
     *
     * @param sms 短信实体
     */
    @Deprecated
    BatchSubmitResp submit(List<Sms> sms);

    /**
     * 主动获取未读短信状态
     * 成功为DELIVRD，无特殊要求一次最多返回500条，可以用msgId来匹配返回的状态
     * 可提供回掉URL自动推送
     */
    @Deprecated
    ReportResp getReport();

    /**
     * 主动获取未读上行
     * 无特殊要求一次最多返回500条
     * 可提供回掉URL自动推送
     */
    @Deprecated
    DeliverResp getDeliver();

    /**
     * V4接口 预付费账号余额查询
     *
     * @return
     */
    default BalanceResp getBalance() {
        return null;
    }

    ;

    /**
     * V4接口 获取发送账号日统计
     *
     * @param date 查询的日期  日期格式化：yyyyMMdd
     * @return
     */
    default DailyStatsResp getDailyStats(String date) {
        return null;
    }

    ;
}
