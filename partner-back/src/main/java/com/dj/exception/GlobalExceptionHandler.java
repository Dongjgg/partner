package com.dj.exception;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.util.StrUtil;
import com.dj.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SaTokenException.class)
    public Result notLoginException(SaTokenException e) {
        log.error("权限验证错误", e);
        return Result.error("401", "请登录");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg;
        try {
            msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        } catch (Exception be) {
            msg = "";
        }
        log.warn("参数校验错误", e);
        return Result.error(msg);
    }

    @ExceptionHandler(value = ServiceException.class)
    public Result serviceExceptionError(ServiceException e) {
        String code = e.getCode();
        if (StrUtil.isNotBlank(code)) {
            return Result.error(code, e.getMessage());
        }
        return Result.error(e.getMessage());
    }


//    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
//    public Result sQLIntegrityConstraintViolationException(Exception e){
//        log.error("数据库错误", e);
//        return Result.error("未知错误");
//    }


    @ExceptionHandler(value = Exception.class)
    public Result exceptionError(Exception e) {
        log.error("未知错误", e);
        return Result.error("未知错误");
    }

}
