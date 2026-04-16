package com.awsome.shop.order.application.impl.service.order;

import com.awsome.shop.order.application.api.dto.order.OrderDTO;
import com.awsome.shop.order.application.api.dto.order.request.*;
import com.awsome.shop.order.application.api.service.order.OrderApplicationService;
import com.awsome.shop.order.application.impl.client.ServiceClient;
import com.awsome.shop.order.application.impl.email.EmailService;
import com.awsome.shop.order.common.dto.PageResult;
import com.awsome.shop.order.common.exception.BusinessException;
import com.awsome.shop.order.common.util.RedemptionCodeGenerator;
import com.awsome.shop.order.domain.model.order.OrderEntity;
import com.awsome.shop.order.domain.service.order.OrderDomainService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderDomainService orderDomainService;
    private final ServiceClient serviceClient;
    private final EmailService emailService;

    @Override
    public OrderDTO createOrder(CreateOrderRequest request) {
        // 调用 Product Service 获取商品信息
        JsonNode product = serviceClient.getProduct(request.getProductId());
        String productName = "未知商品";
        int pointsAmount = 0;
        if (product != null) {
            productName = product.has("name") ? product.get("name").asText() : productName;
            pointsAmount = product.has("pointsPrice") ? product.get("pointsPrice").asInt() : 0;
            int status = product.has("status") ? product.get("status").asInt() : 0;
            if (status != 1) {
                throw new BusinessException("BIZ_001", "商品未上架");
            }
            int stock = product.has("stock") ? product.get("stock").asInt() : 0;
            if (stock <= 0) {
                throw new BusinessException("BIZ_002", "商品库存不足");
            }
        }

        OrderEntity order = new OrderEntity();
        order.setUserId(request.getOperatorId());
        order.setUserName(serviceClient.getUserDisplayName(request.getOperatorId()));
        order.setProductId(request.getProductId());
        order.setProductName(productName);
        order.setPointsAmount(pointsAmount);

        OrderEntity created = orderDomainService.create(order);
        return toDTO(created);
    }

    @Override
    public OrderDTO confirmOrder(ConfirmOrderRequest request) {
        OrderEntity order = orderDomainService.findById(request.getId());

        // 1. 扣减积分
        boolean pointsDeducted = serviceClient.deductPoints(
                order.getUserId(),
                (long) order.getPointsAmount(),
                "兑换商品: " + order.getProductName(),
                order.getId());
        if (!pointsDeducted) {
            throw new BusinessException("BIZ_003", "积分扣减失败，请检查余额");
        }

        // 2. 扣减库存
        boolean stockDeducted = serviceClient.deductStock(order.getProductId(), 1);
        if (!stockDeducted) {
            log.warn("库存扣减失败，退还积分 userId={} amount={}", order.getUserId(), order.getPointsAmount());
            serviceClient.grantPoints(order.getUserId(), (long) order.getPointsAmount(),
                    "库存不足退还: " + order.getProductName());
            throw new BusinessException("BIZ_004", "库存扣减失败，积分已退还");
        }

        // 3. 生成兑换码
        String redemptionCode = RedemptionCodeGenerator.generate();

        // 4. 获取用户邮箱
        String userEmail = serviceClient.getUserEmail(order.getUserId());
        if (userEmail == null || userEmail.isBlank()) {
            // 补偿：退还积分+库存
            log.warn("获取用户邮箱失败，退还积分和库存 userId={}", order.getUserId());
            serviceClient.grantPoints(order.getUserId(), (long) order.getPointsAmount(),
                    "邮箱获取失败退还: " + order.getProductName());
            serviceClient.deductStock(order.getProductId(), -1);
            throw new BusinessException("BIZ_005", "获取用户邮箱失败，订单确认取消");
        }

        // 5. 发送兑换码邮件（失败则订单确认也失败 — 强一致）
        try {
            emailService.sendRedemptionEmail(userEmail, order.getProductName(),
                    redemptionCode, order.getCreatedAt());
        } catch (Exception e) {
            // 补偿：退还积分+库存
            log.error("邮件发送失败，退还积分和库存 userId={}", order.getUserId(), e);
            serviceClient.grantPoints(order.getUserId(), (long) order.getPointsAmount(),
                    "邮件发送失败退还: " + order.getProductName());
            serviceClient.deductStock(order.getProductId(), -1);
            throw new BusinessException("BIZ_006", "邮件发送失败，订单确认取消");
        }

        // 6. 保存兑换码和邮箱到订单
        order.setRedemptionCode(redemptionCode);
        order.setUserEmail(userEmail);

        // 7. 更新订单状态
        OrderEntity confirmed = orderDomainService.confirm(request.getId());
        confirmed.setRedemptionCode(redemptionCode);
        confirmed.setUserEmail(userEmail);
        orderDomainService.update(confirmed);

        return toDTO(confirmed);
    }

    @Override
    public OrderDTO rejectOrder(RejectOrderRequest request) {
        return toDTO(orderDomainService.reject(request.getId()));
    }

    @Override
    public OrderDTO getOrder(GetOrderRequest request) {
        return toDTO(orderDomainService.findById(request.getId()));
    }

    @Override
    public PageResult<OrderDTO> listMyOrders(ListMyOrdersRequest request) {
        return orderDomainService.listByUser(
                request.getOperatorId(), request.getStatus(),
                request.getPage(), request.getSize()).convert(this::toDTO);
    }

    @Override
    public PageResult<OrderDTO> listAllOrders(ListAllOrdersRequest request) {
        return orderDomainService.listAll(
                request.getStatus(), request.getUserName(),
                request.getPage(), request.getSize()).convert(this::toDTO);
    }

    private OrderDTO toDTO(OrderEntity entity) {
        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setUserName(entity.getUserName());
        dto.setProductId(entity.getProductId());
        dto.setProductName(entity.getProductName());
        dto.setPointsAmount(entity.getPointsAmount());
        dto.setStatus(entity.getStatus());
        dto.setRedemptionCode(entity.getRedemptionCode());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
