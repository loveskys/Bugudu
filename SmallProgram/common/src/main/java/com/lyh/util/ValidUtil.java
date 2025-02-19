package com.lyh.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 通用校验工具类
 */
@Slf4j
public class ValidUtil {

    /**
     * 校验手机号，有效返回true，无效返回false
     */
    public static boolean validPhone(String phone) {
        if(StringUtils.hasText(phone)) {
            String regex = "^1[3-9]\\d{9}$";
            return phone.matches(regex);
        }
        return false;
    }

}
