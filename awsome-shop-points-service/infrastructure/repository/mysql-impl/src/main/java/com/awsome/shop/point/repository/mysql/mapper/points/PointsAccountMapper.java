package com.awsome.shop.point.repository.mysql.mapper.points;

import com.awsome.shop.point.repository.mysql.po.points.PointsAccountPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分账户 Mapper 接口
 */
@Mapper
public interface PointsAccountMapper extends BaseMapper<PointsAccountPO> {

    /**
     * 原子更新余额（正数增加，负数扣减）
     */
    int updateBalance(@Param("userId") Long userId, @Param("amount") Long amount);
}
