package com.awsome.shop.product.common.enums;

/**
 * 商品服务业务错误码
 *
 * @author AI Assistant
 * @since 2025-11-24
 */
public enum ProductErrorCode implements ErrorCode {

    RESOURCE_NOT_FOUND("NOT_FOUND_001", "资源不存在"),
    RESOURCE_ALREADY_EXISTS("CONFLICT_001", "资源已存在: {0}");

    private final String code;
    private final String message;

    ProductErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
