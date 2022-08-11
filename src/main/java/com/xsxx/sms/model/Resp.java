package com.xsxx.sms.model;

import com.alibaba.fastjson.JSONObject;
import com.xsxx.sms.constants.ApiStatus;

/**
 * 反馈原型
 *
 * @author momo
 * @date 2018/02/07
 */
public class Resp {

    /**
     * http 状态码
     */
    private int status;
    /**
     * 提示信息 默认null
     */
    private String msg;

    public Resp() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(ApiStatus apiStatus) {
        this.status = apiStatus.getStatus();
        this.msg = apiStatus.getMsg();
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
