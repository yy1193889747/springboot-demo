package com.cy.enums;

import lombok.Data;

/**
 * Created by cy
 * 2017/12/8 17:39
 */
public enum ResultEnum {

    UNKNOW_ERROR(-1,"未知错误"),;



    private Integer code;

    private String msg;
    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
