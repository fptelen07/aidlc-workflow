package com.awsome.shop.point.domain.impl.service.points;

import com.awsome.shop.point.common.enums.PointsErrorCode;
import com.awsome.shop.point.common.exception.BusinessException;
import com.awsome.shop.point.domain.model.points.PointsRuleEntity;
import com.awsome.shop.point.domain.service.points.PointsRuleDomainService;
import com.awsome.shop.point.repository.points.PointsRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 积分规则领域服务实现
 */
@Service
@RequiredArgsConstructor
public class PointsRuleDomainServiceImpl implements PointsRuleDomainService {

    private final PointsRuleRepository ruleRepository;

    @Override
    public PointsRuleEntity create(PointsRuleEntity rule) {
        return ruleRepository.save(rule);
    }

    @Override
    public PointsRuleEntity update(PointsRuleEntity rule) {
        PointsRuleEntity existing = ruleRepository.findById(rule.getId());
        if (existing == null) {
            throw new BusinessException(PointsErrorCode.RULE_NOT_FOUND);
        }
        return ruleRepository.update(rule);
    }

    @Override
    public void toggleEnabled(Long id) {
        PointsRuleEntity existing = ruleRepository.findById(id);
        if (existing == null) {
            throw new BusinessException(PointsErrorCode.RULE_NOT_FOUND);
        }
        ruleRepository.updateEnabled(id, !existing.getEnabled());
    }

    @Override
    public List<PointsRuleEntity> findAll() {
        return ruleRepository.findAll();
    }
}
