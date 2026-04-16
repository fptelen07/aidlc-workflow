package com.awsome.shop.order.common.enums;

/**
 * 订单相关错误码
 */
public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND("NOT_FOUND_001", "订单不存在"),
    INVALID_STATUS("BIZ_001", "订单状态不允许此操作"),
    INSUFFICIENT_STOCK("BIZ_002", "商品库存不足"),
    INSUFFICIENT_POINTS("BIZ_003", "积分余额不足");

    private final String code;
    private final String message;

    OrderErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}
