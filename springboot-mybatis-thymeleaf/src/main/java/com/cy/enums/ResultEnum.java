package com.cy.enums;


/**
 * Created by cy
 * 2017/12/8 17:39
 */
public enum ResultEnum {

    UNKNOW_ERROR(-1,"未知错误"),
    NO_ACCESS(-2,"拒绝访问"),
    SUCCESS(0, "成功")
    ;


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
