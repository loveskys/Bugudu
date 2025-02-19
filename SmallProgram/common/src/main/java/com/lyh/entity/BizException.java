package com.lyh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BizException extends RuntimeException {
    private Integer code;
    private String msg;

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}