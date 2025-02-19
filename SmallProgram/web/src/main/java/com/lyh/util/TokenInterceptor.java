package com.lyh.util;

import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    private TokenUtil tokenUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
           return true;
        }
        String token = request.getHeader("Authorization");
        String client_flag = request.getHeader("CLIENT_FLAG");
        System.out.println("拦截：Authorization："+token+"，CLIENT_FLAG："+client_flag);
        if (!StringUtils.hasText(token)) {
            throw new BizException(ComResponse.UNAUTHORIZED, "无令牌，请登录！");
        }
        if (!StringUtils.hasText(client_flag)) {
            throw new BizException(ComResponse.UNAUTHORIZED, "无CLIENT_FLAG！");
        }

        if (client_flag.equals("SMALL_PC")) {
            tokenUtil.checkToken(token, TokenUtil.ADMIN_TOKEN);
        } else if (client_flag.equals("SMALL_WEB")) {
            tokenUtil.checkToken(token, TokenUtil.STUDENT_TOKEN);
        } else {
            throw new BizException(ComResponse.UNAUTHORIZED, "CLIENT_FLAG不正确！");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.clear();
    }
}