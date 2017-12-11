package com.cy.exception;

import com.cy.enums.ResultEnum;

/**
 * Created by cy
 * 2017/12/11 8:31
 */
public class AllException extends RuntimeException {

    public AllException (ResultEnum resultEnum){
        super(resultEnum.getMsg());
    }
    public AllException (String message){
        super(message);
    }
}
