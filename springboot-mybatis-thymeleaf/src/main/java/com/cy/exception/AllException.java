package com.cy.exception;

import com.cy.enums.ResultEnum;

/**
 * Created by cy
 * 2017/12/11 8:31
 */
public class AllException extends RuntimeException {

    private Integer code;

    public AllException (ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
    public AllException (String message){
        super(message);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
