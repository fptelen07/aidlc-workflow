package com.awsome.shop.auth.common.enums;

/**
 * 认证相关错误码
 */
public enum AuthErrorCode implements ErrorCode {

    INVALID_CREDENTIALS("AUTH_001", "用户名或密码错误"),
    TOKEN_EXPIRED("AUTH_002", "Token 已过期，请重新登录"),
    INVALID_TOKEN("AUTH_003", "无效的 Token");

    private final String code;
    private final String message;

    AuthErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}
