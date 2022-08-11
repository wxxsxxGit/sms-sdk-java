package com.xsxx.sms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 未读上行
 *
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
