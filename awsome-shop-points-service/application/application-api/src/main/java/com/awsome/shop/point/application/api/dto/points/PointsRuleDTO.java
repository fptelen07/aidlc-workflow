package com.awsome.shop.point.application.api.dto.points;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分规则 DTO
 */
@Data
public class PointsRuleDTO {

    private Long id;
    private String name;
    private String type;
    private Long amount;
    private Boolean enabled;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
