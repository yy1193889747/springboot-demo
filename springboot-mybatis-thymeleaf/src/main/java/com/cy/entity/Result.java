package com.cy.entity;

import lombok.Data;

/**
 * Created by cy
 * 2017/12/8 17:29
 */
@Data
public class Result<T> {

    /**
     * 返回错误码
     */
    private Integer code;
    /**
     * 返回提示信息
     */
    private String msg;
    /**
     * 具体内容
     */
    private T data;

}
