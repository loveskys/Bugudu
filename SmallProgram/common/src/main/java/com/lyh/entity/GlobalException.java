package com.lyh.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            BindException.class, ServletRequestBindingException.class, MethodArgumentNotValidException.class})
    public ComResponse<?> handleHttpMessageNotReadableException(Exception e) {
        log.error("请求参数解析失败", e);
        if (e instanceof BindException){
            return new ComResponse<>().error(403, ((BindException)e).getAllErrors().getFirst().getDefaultMessage(),"请求参数解析失败");
        }
        return new ComResponse<>().error(405, e.getMessage(),"405异常");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ComResponse<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("找不到所请求的路径", e);
        return new ComResponse<>().error(404, e.getMessage(),"找不到所请求的路径");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ComResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法: {}", e.getMethod(), e);
        return new ComResponse<>().error(405,"不支持当前请求方法", e.getMethod());
    }


    @ExceptionHandler(MultipartException.class)
    public ComResponse<?> FileHandleError(MultipartException e) {
        log.error(e.getMessage(),e);
        return new ComResponse<>().error(502,"文件上传错误", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ComResponse<?> handleException(Throwable e) {
        log.error(e.getMessage(), e);
        e.printStackTrace();
        return new ComResponse<>().error(500, "服务器运行异常", e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public ComResponse<?> bizExceptionHandler(BizException e) {
        log.error(e.getMsg());
        return new ComResponse<>().error(e.getCode(), e.getMsg(), e.getMessage());
    }
}