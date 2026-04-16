package com.awsome.shop.order.application.api.service.order;

import com.awsome.shop.order.application.api.dto.order.OrderDTO;
import com.awsome.shop.order.application.api.dto.order.request.*;
import com.awsome.shop.order.common.dto.PageResult;

/**
 * Order 应用服务接口
 */
public interface OrderApplicationService {

    OrderDTO createOrder(CreateOrderRequest request);

    OrderDTO confirmOrder(ConfirmOrderRequest request);

    OrderDTO rejectOrder(RejectOrderRequest request);

    OrderDTO getOrder(GetOrderRequest request);

    PageResult<OrderDTO> listMyOrders(ListMyOrdersRequest request);

    PageResult<OrderDTO> listAllOrders(ListAllOrdersRequest request);
}
