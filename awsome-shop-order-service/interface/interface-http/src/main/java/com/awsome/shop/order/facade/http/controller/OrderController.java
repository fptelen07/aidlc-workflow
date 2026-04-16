package com.awsome.shop.order.facade.http.controller;

import com.awsome.shop.order.application.api.dto.order.OrderDTO;
import com.awsome.shop.order.application.api.dto.order.request.*;
import com.awsome.shop.order.application.api.service.order.OrderApplicationService;
import com.awsome.shop.order.common.dto.PageResult;
import com.awsome.shop.order.facade.http.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "订单管理")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @Operation(summary = "创建订单")
    @PostMapping("/create")
    public Result<OrderDTO> create(@RequestBody @Valid CreateOrderRequest request) {
        return Result.success(orderApplicationService.createOrder(request));
    }

    @Operation(summary = "获取订单详情")
    @PostMapping("/get")
    public Result<OrderDTO> get(@RequestBody @Valid GetOrderRequest request) {
        return Result.success(orderApplicationService.getOrder(request));
    }

    @Operation(summary = "我的订单列表")
    @PostMapping("/my-list")
    public Result<PageResult<OrderDTO>> myList(@RequestBody @Valid ListMyOrdersRequest request) {
        return Result.success(orderApplicationService.listMyOrders(request));
    }

    @Operation(summary = "所有订单列表（管理员）")
    @PostMapping("/list")
    public Result<PageResult<OrderDTO>> list(@RequestBody @Valid ListAllOrdersRequest request) {
        return Result.success(orderApplicationService.listAllOrders(request));
    }

    @Operation(summary = "确认订单（管理员）")
    @PostMapping("/confirm")
    public Result<OrderDTO> confirm(@RequestBody @Valid ConfirmOrderRequest request) {
        return Result.success(orderApplicationService.confirmOrder(request));
    }

    @Operation(summary = "拒绝订单（管理员）")
    @PostMapping("/reject")
    public Result<OrderDTO> reject(@RequestBody @Valid RejectOrderRequest request) {
        return Result.success(orderApplicationService.rejectOrder(request));
    }
}
