package com.example.demo.advice;

import com.example.demo.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

/**
 * @version V1.0.0
 * @ClassName: {@link Result}
 * @Description: 全局异常处理
 * {@link RestController}
 * @author: 兰州
 * @date: 2019/7/16 9:22
 * @Copyright: @2019 All rights reserved.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        log.error("请求出错 , 报错原因为: ", ex);
        return Result.error(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = {AccessDeniedException.class})
    public Result unauthorizedExceptionHandler(HttpServletRequest request, Exception e) {
        return Result.unauthorized("暂无权限");
    }

}
