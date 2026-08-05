package com.sixth.soul_trail.utils;

public class SecurityUtil {
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getCurrentUserId() {
        return userIdHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
    }
}