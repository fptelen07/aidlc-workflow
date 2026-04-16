package com.awsome.shop.point.repository.mysql.mapper.points;

import com.awsome.shop.point.repository.mysql.po.points.PointsTransactionPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 积分交易 Mapper 接口
 */
@Mapper
public interface PointsTransactionMapper extends BaseMapper<PointsTransactionPO> {

    /**
     * 按用户分页查询
     */
    IPage<PointsTransactionPO> selectPageByUserId(IPage<PointsTransactionPO> page,
                                                    @Param("userId") Long userId,
                                                    @Param("type") String type);

    /**
     * 全量分页查询
     */
    IPage<PointsTransactionPO> selectPageAll(IPage<PointsTransactionPO> page,
                                              @Param("type") String type);

    /**
     * 统计指定月份的发放总量
     */
    Long sumAmountByTypeAndMonth(@Param("type") String type,
                                 @Param("year") int year,
                                 @Param("month") int month);
}
