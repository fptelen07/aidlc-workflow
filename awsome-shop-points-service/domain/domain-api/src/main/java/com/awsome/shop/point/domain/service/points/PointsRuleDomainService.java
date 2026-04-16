package com.awsome.shop.point.domain.service.points;

import com.awsome.shop.point.domain.model.points.PointsRuleEntity;

import java.util.List;

/**
 * 积分规则领域服务接口
 */
public interface PointsRuleDomainService {

    PointsRuleEntity create(PointsRuleEntity rule);

    PointsRuleEntity update(PointsRuleEntity rule);

    void toggleEnabled(Long id);

    List<PointsRuleEntity> findAll();
}
