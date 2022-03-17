package com.wq.util.exception;

import com.wq.common.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : yixihan
 * @create : 2022-02-16-10:14
 */
@Slf4j
@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 因为前后端分离 返回一个状态 一般是401 没有权限
     * 捕获运行时异常ShiroException是大部分异常的父类
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e) {
        log.warn ("无权限访问. 异常信息 : " + e.getMessage () + ", 异常原因 : " + e.toString ());
        return Result.fail (e.getMessage ());
    }


    /**
     * 因为前后端分离 返回一个状态
     * 捕获运行时异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public Result handler(HttpServletRequest req, RuntimeException e) {
        log.warn ("运行时异常. 异常信息 : " + e.getMessage () + ", 异常原因 : " + e.toString ());
        return Result.fail (e.getMessage ());
    }

    /**
     * 断言异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.warn ("断言异常. 异常信息 : " + e.getMessage () + ", 异常原因 : " + e.toString ());
        return Result.fail (e.getMessage ());
    }

    /**
     * 处理空指针的异常
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("空指针异常. 异常信息 : " + e.getMessage () + ", 异常原因 : " + e.toString ());
        e.printStackTrace ();
        return Result.fail (e.getMessage ());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        log.error("未知异常. 异常信息 : " + e.getMessage () + ", 异常原因 : " + e.toString ());
        return Result.fail (e.getMessage ());
    }
}
