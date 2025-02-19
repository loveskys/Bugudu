package com.lyh.util;

import com.lyh.entity.user.AdminUser;
import com.lyh.entity.user.StudentUser;

public class ThreadLocalUtil {
    private ThreadLocalUtil() {
    }
    public static void clear() {
        STUDENT_USER_INFO.remove();
        STUDENT_TOKEN.remove();
        ADMIN_USER_INFO.remove();
        ADMIN_TOKEN.remove();
    }

    public static final ThreadLocal<StudentUser> STUDENT_USER_INFO = new ThreadLocal<>();
    public static StudentUser getStudentUser() {
        return STUDENT_USER_INFO.get();
    }
    public static void setStudentUser(StudentUser user) {
        STUDENT_USER_INFO.set(user);
    }
    public static String getStudentUserId() {
        if (STUDENT_USER_INFO.get() != null) {
            return STUDENT_USER_INFO.get().getId();
        }
        return null;
    }
    public static String getStudentUserName() {
        if (STUDENT_USER_INFO.get() != null) {
            return STUDENT_USER_INFO.get().getUserName();
        }
        return null;
    }


    public static final ThreadLocal<AdminUser> ADMIN_USER_INFO = new ThreadLocal<>();
    public static AdminUser getAdminUser() {
        return ADMIN_USER_INFO.get();
    }
    public static void setAdminUser(AdminUser user) {
        ADMIN_USER_INFO.set(user);
    }
    public static String getAdminUserId() {
        if (ADMIN_USER_INFO.get() != null) {
            return ADMIN_USER_INFO.get().getId();
        }
        return null;
    }
    public static String getAdminUserName() {
        if (ADMIN_USER_INFO.get() != null) {
            return ADMIN_USER_INFO.get().getUserName();
        }
        return null;
    }


    private static final ThreadLocal<String> STUDENT_TOKEN = new ThreadLocal<>();
    private static final ThreadLocal<String> ADMIN_TOKEN = new ThreadLocal<>();
    public static String getStudentToken() {
        return STUDENT_TOKEN.get();
    }
    public static void setStudentToken(String token) {
        STUDENT_TOKEN.set(token);
    }
    public static String getAdminToken() {
        return ADMIN_TOKEN.get();
    }
    public static void setAdminToken(String token) {
        ADMIN_TOKEN.set(token);
    }
}