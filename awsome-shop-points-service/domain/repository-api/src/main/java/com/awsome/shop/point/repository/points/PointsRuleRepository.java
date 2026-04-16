package com.awsome.shop.point.repository.points;

import com.awsome.shop.point.domain.model.points.PointsRuleEntity;

import java.util.List;

/**
 * 积分规则仓储接口
 */
public interface PointsRuleRepository {

    PointsRuleEntity save(PointsRuleEntity rule);

    PointsRuleEntity update(PointsRuleEntity rule);

    PointsRuleEntity findById(Long id);

    List<PointsRuleEntity> findAll();

    void updateEnabled(Long id, boolean enabled);
}
