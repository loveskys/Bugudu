package com.lyh.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Aspect
@Component
public class ValidateAop {

    @Around("execution(public * com.lyh.controller.*.*(..))")
    public Object validataObject(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                try {
                    Method getNameMethod = arg.getClass().getMethod("validate");
                    Object result = getNameMethod.invoke(arg);
                    if(result != null) {
                        return result;
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
            }
        }
        return joinPoint.proceed();
    }

}