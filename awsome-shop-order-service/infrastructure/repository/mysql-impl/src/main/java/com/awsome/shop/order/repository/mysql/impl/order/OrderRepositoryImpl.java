package com.awsome.shop.order.repository.mysql.impl.order;

import com.awsome.shop.order.common.dto.PageResult;
import com.awsome.shop.order.domain.model.order.OrderEntity;
import com.awsome.shop.order.repository.mysql.mapper.order.OrderMapper;
import com.awsome.shop.order.repository.mysql.po.order.OrderPO;
import com.awsome.shop.order.repository.order.OrderRepository;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

/**
 * Order 仓储实现
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderMapper orderMapper;

    @Override
    public void save(OrderEntity entity) {
        OrderPO po = toPO(entity);
        orderMapper.insert(po);
        entity.setId(po.getId());
    }

    @Override
    public void update(OrderEntity entity) {
        OrderPO po = toPO(entity);
        orderMapper.updateById(po);
    }

    @Override
    public OrderEntity findById(Long id) {
        OrderPO po = orderMapper.selectById(id);
        return po == null ? null : toEntity(po);
    }

    @Override
    public PageResult<OrderEntity> pageByUserId(Long userId, String status, int page, int size) {
        IPage<OrderPO> result = orderMapper.pageByUserId(new Page<>(page, size), userId, status);
        return toPageResult(result);
    }

    @Override
    public PageResult<OrderEntity> pageAll(String status, String userName, int page, int size) {
        IPage<OrderPO> result = orderMapper.pageAll(new Page<>(page, size), status, userName);
        return toPageResult(result);
    }

    private PageResult<OrderEntity> toPageResult(IPage<OrderPO> result) {
        PageResult<OrderEntity> pageResult = new PageResult<>();
        pageResult.setCurrent(result.getCurrent());
        pageResult.setSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setPages(result.getPages());
        pageResult.setRecords(result.getRecords().stream().map(this::toEntity).collect(Collectors.toList()));
        return pageResult;
    }

    private OrderEntity toEntity(OrderPO po) {
        OrderEntity entity = new OrderEntity();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setUserName(po.getUserName());
        entity.setProductId(po.getProductId());
        entity.setProductName(po.getProductName());
        entity.setPointsAmount(po.getPointsAmount());
        entity.setStatus(po.getStatus());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private OrderPO toPO(OrderEntity entity) {
        OrderPO po = new OrderPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setUserName(entity.getUserName());
        po.setProductId(entity.getProductId());
        po.setProductName(entity.getProductName());
        po.setPointsAmount(entity.getPointsAmount());
        po.setStatus(entity.getStatus());
        return po;
    }
}
