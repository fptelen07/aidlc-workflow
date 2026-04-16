package com.awsome.shop.point.repository.mysql.impl.points;

import com.awsome.shop.point.domain.model.points.PointsRuleEntity;
import com.awsome.shop.point.repository.mysql.mapper.points.PointsRuleMapper;
import com.awsome.shop.point.repository.mysql.po.points.PointsRulePO;
import com.awsome.shop.point.repository.points.PointsRuleRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分规则仓储实现
 */
@Repository
@RequiredArgsConstructor
public class PointsRuleRepositoryImpl implements PointsRuleRepository {

    private final PointsRuleMapper ruleMapper;

    @Override
    public PointsRuleEntity save(PointsRuleEntity rule) {
        PointsRulePO po = toPO(rule);
        ruleMapper.insert(po);
        rule.setId(po.getId());
        return findById(po.getId());
    }

    @Override
    public PointsRuleEntity update(PointsRuleEntity rule) {
        PointsRulePO po = toPO(rule);
        ruleMapper.updateById(po);
        return findById(rule.getId());
    }

    @Override
    public PointsRuleEntity findById(Long id) {
        PointsRulePO po = ruleMapper.selectById(id);
        return po == null ? null : toEntity(po);
    }

    @Override
    public List<PointsRuleEntity> findAll() {
        LambdaQueryWrapper<PointsRulePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PointsRulePO::getCreatedAt);
        return ruleMapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void updateEnabled(Long id, boolean enabled) {
        PointsRulePO po = new PointsRulePO();
        po.setId(id);
        po.setEnabled(enabled);
        ruleMapper.updateById(po);
    }

    private PointsRuleEntity toEntity(PointsRulePO po) {
        PointsRuleEntity entity = new PointsRuleEntity();
        entity.setId(po.getId());
        entity.setName(po.getName());
        entity.setType(po.getType());
        entity.setAmount(po.getAmount());
        entity.setEnabled(po.getEnabled());
        entity.setDescription(po.getDescription());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private PointsRulePO toPO(PointsRuleEntity entity) {
        PointsRulePO po = new PointsRulePO();
        po.setId(entity.getId());
        po.setName(entity.getName());
        po.setType(entity.getType());
        po.setAmount(entity.getAmount());
        po.setEnabled(entity.getEnabled());
        po.setDescription(entity.getDescription());
        return po;
    }
}
