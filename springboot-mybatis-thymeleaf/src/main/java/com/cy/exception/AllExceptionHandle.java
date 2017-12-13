package com.cy.exception;

import com.cy.entity.Result;
import com.cy.util.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cy
 * 2017/12/11 8:37
 */
@ControllerAdvice
@Log4j2
public class AllExceptionHandle {

    /**
     * 处理全局异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handlerGlobalException(Exception exception){
        if (exception instanceof AllException) {
            AllException allException = (AllException) exception;
            return ResultUtil.error(allException.getCode(), allException.getMessage());
        }else {
            log.error("【系统异常】{}", exception.getMessage());
            return ResultUtil.error(-1, "未知错误");
        }
    }
}
