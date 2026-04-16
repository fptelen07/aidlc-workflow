package com.awsome.shop.point.facade.http.controller;

import com.awsome.shop.point.application.api.dto.points.BalanceDTO;
import com.awsome.shop.point.application.api.dto.points.StatisticsDTO;
import com.awsome.shop.point.application.api.dto.points.TransactionDTO;
import com.awsome.shop.point.application.api.dto.points.request.*;
import com.awsome.shop.point.application.api.service.points.PointsApplicationService;
import com.awsome.shop.point.common.dto.PageResult;
import com.awsome.shop.point.facade.http.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "积分管理", description = "积分账户与交易管理")
@RestController
@RequestMapping("/api/v1/point")
@RequiredArgsConstructor
public class PointsController {

    private final PointsApplicationService pointsApplicationService;

    @Operation(summary = "查询积分余额")
    @PostMapping("/balance")
    public Result<BalanceDTO> getBalance(@RequestBody GetBalanceRequest request) {
        return Result.success(pointsApplicationService.getBalance(request));
    }

    @Operation(summary = "查询我的积分历史")
    @PostMapping("/my-history")
    public Result<PageResult<TransactionDTO>> getMyHistory(
            @RequestBody @Valid GetMyHistoryRequest request) {
        return Result.success(pointsApplicationService.getMyHistory(request));
    }

    @Operation(summary = "发放积分（管理员）")
    @PostMapping("/grant")
    public Result<Void> grant(@RequestBody @Valid GrantPointsRequest request) {
        pointsApplicationService.grant(request);
        return Result.success();
    }

    @Operation(summary = "批量发放积分（管理员）")
    @PostMapping("/batch-grant")
    public Result<Void> batchGrant(@RequestBody @Valid BatchGrantRequest request) {
        pointsApplicationService.batchGrant(request);
        return Result.success();
    }

    @Operation(summary = "扣减积分")
    @PostMapping("/deduct")
    public Result<Void> deduct(@RequestBody @Valid DeductPointsRequest request) {
        pointsApplicationService.deduct(request);
        return Result.success();
    }

    @Operation(summary = "查询所有积分历史（管理员）")
    @PostMapping("/history")
    public Result<PageResult<TransactionDTO>> getAllHistory(
            @RequestBody @Valid GetAllHistoryRequest request) {
        return Result.success(pointsApplicationService.getAllHistory(request));
    }

    @Operation(summary = "积分统计（管理员）")
    @PostMapping("/statistics")
    public Result<StatisticsDTO> getStatistics(@RequestBody GetStatisticsRequest request) {
        return Result.success(pointsApplicationService.getStatistics(request));
    }
}
