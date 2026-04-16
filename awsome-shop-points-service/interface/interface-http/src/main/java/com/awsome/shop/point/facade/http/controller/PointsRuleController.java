package com.awsome.shop.point.facade.http.controller;

import com.awsome.shop.point.application.api.dto.points.PointsRuleDTO;
import com.awsome.shop.point.application.api.dto.points.request.CreateRuleRequest;
import com.awsome.shop.point.application.api.dto.points.request.ToggleRuleRequest;
import com.awsome.shop.point.application.api.dto.points.request.UpdateRuleRequest;
import com.awsome.shop.point.application.api.service.points.PointsRuleApplicationService;
import com.awsome.shop.point.facade.http.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "积分规则管理", description = "积分规则配置")
@RestController
@RequestMapping("/api/v1/point/rule")
@RequiredArgsConstructor
public class PointsRuleController {

    private final PointsRuleApplicationService ruleApplicationService;

    @Operation(summary = "创建积分规则（管理员）")
    @PostMapping("/create")
    public Result<PointsRuleDTO> createRule(@RequestBody @Valid CreateRuleRequest request) {
        return Result.success(ruleApplicationService.createRule(request));
    }

    @Operation(summary = "更新积分规则（管理员）")
    @PostMapping("/update")
    public Result<PointsRuleDTO> updateRule(@RequestBody @Valid UpdateRuleRequest request) {
        return Result.success(ruleApplicationService.updateRule(request));
    }

    @Operation(summary = "切换积分规则启用状态（管理员）")
    @PostMapping("/toggle")
    public Result<Void> toggleRule(@RequestBody @Valid ToggleRuleRequest request) {
        ruleApplicationService.toggleRule(request);
        return Result.success();
    }

    @Operation(summary = "查询所有积分规则（管理员）")
    @PostMapping("/list")
    public Result<List<PointsRuleDTO>> listRules() {
        return Result.success(ruleApplicationService.listRules());
    }
}
