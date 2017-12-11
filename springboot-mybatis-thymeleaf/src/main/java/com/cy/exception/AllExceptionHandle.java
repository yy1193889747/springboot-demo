package com.cy.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by cy
 * 2017/12/11 8:37
 */

@ControllerAdvice
public class AllExceptionHandle {

    /**
     * 处理全局异常
     */
    @ExceptionHandler(Exception.class)
    public String handlerGlobalException(Exception exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
}
