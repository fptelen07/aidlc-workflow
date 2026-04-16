package com.awsome.shop.point.application.api.service.points;

import com.awsome.shop.point.application.api.dto.points.PointsRuleDTO;
import com.awsome.shop.point.application.api.dto.points.request.CreateRuleRequest;
import com.awsome.shop.point.application.api.dto.points.request.ToggleRuleRequest;
import com.awsome.shop.point.application.api.dto.points.request.UpdateRuleRequest;

import java.util.List;

/**
 * 积分规则应用服务接口
 */
public interface PointsRuleApplicationService {

    PointsRuleDTO createRule(CreateRuleRequest request);

    PointsRuleDTO updateRule(UpdateRuleRequest request);

    void toggleRule(ToggleRuleRequest request);

    List<PointsRuleDTO> listRules();
}
