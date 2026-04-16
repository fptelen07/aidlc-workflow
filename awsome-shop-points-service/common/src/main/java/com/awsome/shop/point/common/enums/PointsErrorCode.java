package com.awsome.shop.point.common.enums;

/**
 * 积分服务业务错误码
 */
public enum PointsErrorCode implements ErrorCode {

    ACCOUNT_NOT_FOUND("NOT_FOUND_001", "积分账户不存在"),
    RULE_NOT_FOUND("NOT_FOUND_002", "积分规则不存在"),
    INSUFFICIENT_BALANCE("BIZ_001", "积分余额不足"),
    INVALID_AMOUNT("PARAM_001", "积分数量必须大于0"),
    INVALID_USER_IDS("PARAM_002", "用户ID列表不能为空");

    private final String code;
    private final String message;

    PointsErrorCode(String code, String message) {
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
