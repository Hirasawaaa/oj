package org.example.oj.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<Void>handleBusiness(BusinessException e){
        log.warn("业务异常: code={}, msg={}",e.getCode(),e.getMessage());
        return Result.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void>handleBusiness(Exception e){
        log.error("未知异常",e);
        return Result.error(500,"服务器内部错误");
    }
}
