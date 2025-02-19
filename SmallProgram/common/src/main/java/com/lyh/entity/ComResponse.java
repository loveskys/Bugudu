package com.lyh.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * 统一返回类
 */
@Data
@AllArgsConstructor
public class ComResponse<T> implements Serializable {

    /**
     * 成功
     */
    public static final Integer SUCCESS = 200;
    /**
     * 失败
     */
    public static final Integer FAIL = 400;
    /**
     * 未认证
     */
    public static final Integer UNAUTHORIZED = 401;
    /**
     * 未授权
     */
    public static final Integer FORBIDDEN = 403;
    /**
     * 404
     */
    public static final Integer NOT_FOUND = 404;
    /**
     * 系统错误
     */
    public static final Integer ERROR = 500;
    /**
     * 响应状态码：200-成功,400-失败,401-未认证,403-未授权,404-找不到网页,500-系统错误
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;
    public ComResponse() {
    }


    public ComResponse<T> success(T t) {
        this.code = SUCCESS;
        this.msg = "操作成功";
        this.data = t;
        return this;
    }

    public ComResponse<T> success(String msg, T t) {
        this.code = SUCCESS;
        this.msg = msg;
        this.data = t;
        return this;
    }

    public ComResponse<T> success() {
        this.code = SUCCESS;
        this.msg = "操作成功";
        return this;
    }

    public ComResponse<T> success(String msg) {
        this.code = SUCCESS;
        this.msg = msg;
        return this;
    }

    public ComResponse<T> fail() {
        this.code = FAIL;
        this.msg = "操作失败";
        return this;
    }

    public ComResponse<T> fail(String msg) {
        this.code = FAIL;
        this.msg = msg;
        return this;
    }

    public ComResponse<T> error() {
        this.code = ERROR;
        this.msg = "系统错误";
        return this;
    }

    public ComResponse<T> error(int ERROR, String msg, T eroMsg) {
        this.code = ERROR;
        this.msg = msg;
        this.data = eroMsg;
        return this;
    }

    public ComResponse<T> error(String msg) {
        this.code = ERROR;
        this.msg = msg;
        return this;
    }

    public ComResponse<T> forbidden() {
        this.code = FORBIDDEN;
        this.msg = "没有相关权限";
        return this;
    }

    public ComResponse<T> forbidden(String msg) {
        this.code = FORBIDDEN;
        this.msg = msg;
        return this;
    }

    public ComResponse<T> unauthorized() {
        this.code = UNAUTHORIZED;
        this.msg = "未认证";
        return this;
    }

    public ComResponse<T> unauthorized(String msg) {
        this.code = UNAUTHORIZED;
        this.msg = msg;
        return this;
    }

    public ComResponse<T> notFound(String msg) {
        this.code = NOT_FOUND;
        this.msg = msg;
        return this;
    }
}