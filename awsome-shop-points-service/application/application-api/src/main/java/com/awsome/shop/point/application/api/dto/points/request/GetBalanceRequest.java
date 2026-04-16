package com.awsome.shop.point.application.api.dto.points.request;

import lombok.Data;

/**
 * 查询积分余额请求
 */
@Data
public class GetBalanceRequest {

    private Long operatorId;
}
