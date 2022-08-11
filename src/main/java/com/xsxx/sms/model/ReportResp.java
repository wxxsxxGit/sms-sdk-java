package com.xsxx.sms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 状态报告
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
