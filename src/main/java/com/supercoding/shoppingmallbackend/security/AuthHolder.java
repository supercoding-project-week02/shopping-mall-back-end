package com.supercoding.shoppingmallbackend.security;

public class AuthHolder {
    private static final ThreadLocal<Long> userIdxHolder = new ThreadLocal<>();

    public static void setUserIdx(Long userIdx) { //TODO: porfile로 명칭 변경 예정
        userIdxHolder.set(userIdx);
    }

    public static Long getUserIdx() {
        return userIdxHolder.get();
    }

    public static void clearUserIdx() {
        userIdxHolder.remove();
    }
}
