package com.awsome.shop.point.domain.model.points;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 积分规则领域实体
 */
@Data
public class PointsRuleEntity {

    private Long id;
    private String name;
    private String type;
    private Long amount;
    private Boolean enabled;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
