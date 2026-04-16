package com.awsome.shop.point.application.api.dto.points;

import lombok.Data;

/**
 * 积分统计 DTO
 */
@Data
public class StatisticsDTO {

    private Long monthlyGranted;
    private Long monthlyDeducted;
    private Long monthlyNet;
}
