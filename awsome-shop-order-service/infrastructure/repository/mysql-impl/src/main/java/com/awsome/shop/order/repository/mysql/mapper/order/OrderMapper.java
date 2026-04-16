package com.awsome.shop.order.repository.mysql.mapper.order;

import com.awsome.shop.order.repository.mysql.po.order.OrderPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Order Mapper 接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderPO> {

    /**
     * 按用户分页查询
     */
    IPage<OrderPO> pageByUserId(IPage<OrderPO> page,
                                @Param("userId") Long userId,
                                @Param("status") String status);

    /**
     * 全量分页查询（管理员）
     */
    IPage<OrderPO> pageAll(IPage<OrderPO> page,
                           @Param("status") String status,
                           @Param("userName") String userName);
}
