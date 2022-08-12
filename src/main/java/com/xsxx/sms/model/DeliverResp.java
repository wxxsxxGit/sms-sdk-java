package com.xsxx.sms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 未读上行
 * {
 *     "status" : 0,                             // 固定值0
 *     "result":
 *     [
 *
 *         {
 *             "phone" : "13921350591",         // 手机号码
 *             "extCode" : "682",               // 用户提交短信时候带的extCode
 *             "content" : "上行回复内容" ,       // 上行内容
 *             "receivetime" : "20170816153922",// 短信到达时间
 * 			"sId" : "123456789abcdefg",      // 批次号
 * 			"sign" : "签名",
 * 			"msgid" : "-8629637681836384963" // 短信唯一编号
 *         }
 *         // 这里是数组会有多条，默认最大500条
 * ，平台可配置
 *     ]
 * }
 * @author momo
 * @date 18-8-30 下午8:17
 */
public class DeliverResp extends Resp {

    /**
     * status : 0
     * result : [{"phone":"18262276782","destid":"-6004182853835414462","content":"PHONERR","submitTime":"20171025111347"}]
     */
    private List<Deliver> result = new ArrayList<>();

    public List<Deliver> getResult() {
        return result;
    }

    public void setResult(List<Deliver> result) {
        this.result = result;
    }

}
