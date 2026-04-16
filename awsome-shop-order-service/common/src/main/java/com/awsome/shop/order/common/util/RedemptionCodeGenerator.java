package com.awsome.shop.order.common.util;

import java.security.SecureRandom;

/**
 * 兑换码生成工具类
 * 格式：XXXX-XXXX-XXXX（大写字母+数字）
 */
public class RedemptionCodeGenerator {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private RedemptionCodeGenerator() {
    }

    public static String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            if (i > 0 && i % 4 == 0) {
                sb.append('-');
            }
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
