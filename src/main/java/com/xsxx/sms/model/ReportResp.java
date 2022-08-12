package com.xsxx.sms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 状态报告
 * 响应格式
 * {
 * "status" : 0,                                 // 固定值0
 * "result" :
 * [
 * {
 * "phone" : "13921350591",             // 手机号码
 * "msgid" : "-8629637681836384963",    // 与提交中的msgid 一致
 * "status" : "DELIVRD" ,               // 短信状态，参考附表2
 * "donetime" : "20170816153922",       // 短信到达时间
 * "fee" : 2,                           // 短信计费条数
 * "sId" : "123456789abcdefg"           // 批次号
 * }
 * //这里是数组会有多条，默认最大500条
 * ，平台可配置
 * ]
 * }
 *
 * @author xiashuai
 * @date 2017/10/25
 */
public class ReportResp extends Resp {

    /**
     * status : 0
     * result : [{"phone":"18262276782","msgid":"-6003923918847073213","status":"PHONERR","donetime":"20171025112829"},{"phone":"18262276782","msgid":"-6003895331544751036","status":"PHONERR","donetime":"20171025113004"}]
     */

    private List<Report> result = new ArrayList<>();

    public List<Report> getResult() {
        return result;
    }

    public void setResult(List<Report> result) {
        this.result = result;
    }

}
