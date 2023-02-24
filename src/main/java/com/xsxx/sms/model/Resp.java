package com.xsxx.sms.model;

import cn.hutool.json.JSONUtil;

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

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
