package com.awsome.shop.order.application.impl.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * 跨服务 HTTP 调用客户端
 */
@Slf4j
@Component
public class ServiceClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${services.product-url:http://localhost:8002}")
    private String productUrl;

    @Value("${services.points-url:http://localhost:8003}")
    private String pointsUrl;

    public ServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * 获取商品详情
     */
    public JsonNode getProduct(Long productId) {
        try {
            String resp = webClient.post()
                    .uri(productUrl + "/api/v1/public/product/get")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("id", productId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode root = objectMapper.readTree(resp);
            if (root.get("code").asInt() == 0) {
                return root.get("data");
            }
            log.warn("获取商品失败: {}", resp);
            return null;
        } catch (Exception e) {
            log.error("调用 Product Service 失败", e);
            return null;
        }
    }

    /**
     * 扣减积分
     */
    public boolean deductPoints(Long userId, Long amount, String reason, Long orderId) {
        try {
            String resp = webClient.post()
                    .uri(pointsUrl + "/api/v1/point/deduct")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "userId", userId,
                            "amount", amount,
                            "reason", reason,
                            "orderId", orderId,
                            "operatorId", userId
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode root = objectMapper.readTree(resp);
            return root.get("code").asInt() == 0;
        } catch (Exception e) {
            log.error("调用 Points Service 扣减积分失败", e);
            return false;
        }
    }

    /**
     * 扣减库存
     */
    public boolean deductStock(Long productId, int quantity) {
        try {
            String resp = webClient.post()
                    .uri(productUrl + "/api/v1/product/deduct-stock")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "id", productId,
                            "quantity", quantity,
                            "operatorId", 1
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode root = objectMapper.readTree(resp);
            return root.get("code").asInt() == 0;
        } catch (Exception e) {
            log.error("调用 Product Service 扣减库存失败", e);
            return false;
        }
    }

    /**
     * 退还积分（补偿）
     */
    public boolean grantPoints(Long userId, Long amount, String reason) {
        try {
            String resp = webClient.post()
                    .uri(pointsUrl + "/api/v1/point/grant")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "userId", userId,
                            "amount", amount,
                            "reason", reason,
                            "operatorId", 1
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonNode root = objectMapper.readTree(resp);
            return root.get("code").asInt() == 0;
        } catch (Exception e) {
            log.error("调用 Points Service 退还积分失败", e);
            return false;
        }
    }
}
