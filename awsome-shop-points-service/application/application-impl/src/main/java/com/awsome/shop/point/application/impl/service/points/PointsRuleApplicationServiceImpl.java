package com.awsome.shop.point.application.impl.service.points;

import com.awsome.shop.point.application.api.dto.points.PointsRuleDTO;
import com.awsome.shop.point.application.api.dto.points.request.CreateRuleRequest;
import com.awsome.shop.point.application.api.dto.points.request.ToggleRuleRequest;
import com.awsome.shop.point.application.api.dto.points.request.UpdateRuleRequest;
import com.awsome.shop.point.application.api.service.points.PointsRuleApplicationService;
import com.awsome.shop.point.domain.model.points.PointsRuleEntity;
import com.awsome.shop.point.domain.service.points.PointsRuleDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分规则应用服务实现
 */
@Service
@RequiredArgsConstructor
public class PointsRuleApplicationServiceImpl implements PointsRuleApplicationService {

    private final PointsRuleDomainService ruleDomainService;

    @Override
    public PointsRuleDTO createRule(CreateRuleRequest request) {
        PointsRuleEntity entity = new PointsRuleEntity();
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setAmount(request.getAmount());
        entity.setEnabled(request.getEnabled());
        entity.setDescription(request.getDescription());
        return toDTO(ruleDomainService.create(entity));
    }

    @Override
    public PointsRuleDTO updateRule(UpdateRuleRequest request) {
        PointsRuleEntity entity = new PointsRuleEntity();
        entity.setId(request.getId());
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setAmount(request.getAmount());
        entity.setEnabled(request.getEnabled());
        entity.setDescription(request.getDescription());
        return toDTO(ruleDomainService.update(entity));
    }

    @Override
    public void toggleRule(ToggleRuleRequest request) {
        ruleDomainService.toggleEnabled(request.getId());
    }

    @Override
    public List<PointsRuleDTO> listRules() {
        return ruleDomainService.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PointsRuleDTO toDTO(PointsRuleEntity entity) {
        PointsRuleDTO dto = new PointsRuleDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());
        dto.setEnabled(entity.getEnabled());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
