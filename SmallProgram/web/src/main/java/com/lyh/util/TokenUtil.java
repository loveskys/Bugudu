package com.lyh.util;

import com.lyh.dao.AdminUserMapper;
import com.lyh.dao.StudentUserMapper;
import com.lyh.entity.BizException;
import com.lyh.entity.ComResponse;
import com.lyh.entity.user.AdminUser;
import com.lyh.entity.user.StudentUser;
import io.lettuce.core.RedisCommandTimeoutException;
import io.lettuce.core.RedisConnectionException;
import jakarta.annotation.Resource;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Component
public class TokenUtil {
    @Resource
    private StudentUserMapper studentUserMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String STUDENT_TOKEN = "student_user_token:";
    public static final String ADMIN_TOKEN = "admin_user_token:";
    private static final String TOKEN_FIX = "uid_";
    //token过期时间18000秒 = 5小时
    private static final String EXPIRE_TIME = "18000";
    private static final String CRITICAL = "120";

    public String signToken(String userId, String token_flag) {
        String key = token_flag + TOKEN_FIX + userId;
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>(
                "local token = redis.call('get',KEYS[1]);" +
                        "local tokenttl = redis.call('ttl',KEYS[1]);" +
                        "local critical = tonumber(ARGV[4]);" +
                        "if (token and tokenttl > critical) then return token;" +
                        "elseif(token and tokenttl <= critical) then " +
                        "local uid = redis.call('get',token);" +
                        "redis.call('expire',token,ARGV[3]);" +
                        "redis.call('expire',uid,ARGV[3]);" +
                        "return token;" +
                        "else " + "redis.call('SET',KEYS[1],ARGV[1]);" +
                        "redis.call('SET',KEYS[2],ARGV[2]);" +
                        "redis.call('expire',KEYS[1],ARGV[3]);" +
                        "redis.call('expire',KEYS[2],ARGV[3]);" +
                        "return ARGV[1];" +
                        "end;"
                , String.class);
        String token = token_flag + DigestUtils.md5DigestAsHex((getuid32()).getBytes());
        try {
            token = stringRedisTemplate.execute(defaultRedisScript, Arrays.asList(key, token), token, key, EXPIRE_TIME, CRITICAL);
        } catch (RedisConnectionException | RedisCommandTimeoutException | RedisConnectionFailureException | QueryTimeoutException | RedisSystemException e) {
            throw new BizException(ComResponse.ERROR, "signToken()：redis连接异常："+e.getMessage());
        }
        assert token != null;
        return token.replaceAll(token_flag,"");
    }

    public void checkToken(String token, String token_flag) {
        token = token_flag + token;
        DefaultRedisScript<String> defaultRedisScript = new DefaultRedisScript<>(
                "local uid = redis.call('get',KEYS[1]);" +
                        "local uidttl = redis.call('ttl',KEYS[1]);" +
                        "local critical = tonumber(ARGV[3]);" +
                        "if (uid and uidttl > critical) then return uid;" +
                        "elseif(uid and uidttl <= critical) then " +
                        "redis.call('expire',KEYS[1],ARGV[2]);" +
                        "redis.call('expire',uid,ARGV[2]);" +
                        "return uid; else return nil; end;"
                , String.class);

        String value = "";
        try {
            value = stringRedisTemplate.execute(defaultRedisScript, Collections.singletonList(token), token, EXPIRE_TIME, CRITICAL);
        } catch (RedisConnectionException | RedisCommandTimeoutException | RedisConnectionFailureException | QueryTimeoutException | RedisSystemException e) {
            throw new BizException(ComResponse.ERROR, "checkToken()：redis连接异常："+e.getMessage());
        }
        System.out.println("token： "+value);
        if (StringUtils.hasText(value)) {
            String userId = value.replace(token_flag + TOKEN_FIX, "");
            if(token_flag.equals(STUDENT_TOKEN)) {
                StudentUser studentUser = studentUserMapper.selectById(userId);
                if (studentUser == null) {
                    throw new BizException(ComResponse.UNAUTHORIZED, "用户不存在，请重新登录");
                }
                ThreadLocalUtil.setStudentUser(studentUser);
            }
            if(token_flag.equals(ADMIN_TOKEN)) {
                AdminUser adminUser = adminUserMapper.selectById(userId);
                if (adminUser == null) {
                    throw new BizException(ComResponse.UNAUTHORIZED, "管理员用户不存在，请重新登录");
                }
                ThreadLocalUtil.setAdminUser(adminUser);
            }
        } else {
            throw new BizException(ComResponse.UNAUTHORIZED, "token无效，请重新登录");
        }
    }

    public static String getuid32() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}