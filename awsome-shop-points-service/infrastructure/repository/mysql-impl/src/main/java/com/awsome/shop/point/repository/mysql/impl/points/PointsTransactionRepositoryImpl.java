package com.awsome.shop.point.repository.mysql.impl.points;

import com.awsome.shop.point.common.dto.PageResult;
import com.awsome.shop.point.domain.model.points.PointsTransactionEntity;
import com.awsome.shop.point.repository.mysql.mapper.points.PointsTransactionMapper;
import com.awsome.shop.point.repository.mysql.po.points.PointsTransactionPO;
import com.awsome.shop.point.repository.points.PointsTransactionRepository;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

/**
 * 积分交易仓储实现
 */
@Repository
@RequiredArgsConstructor
public class PointsTransactionRepositoryImpl implements PointsTransactionRepository {

    private final PointsTransactionMapper transactionMapper;

    @Override
    public PointsTransactionEntity save(PointsTransactionEntity tx) {
        PointsTransactionPO po = toPO(tx);
        transactionMapper.insert(po);
        tx.setId(po.getId());
        return tx;
    }

    @Override
    public PageResult<PointsTransactionEntity> pageByUserId(Long userId, String type, int page, int size) {
        IPage<PointsTransactionPO> result = transactionMapper.selectPageByUserId(
                new Page<>(page, size), userId, type);
        return toPageResult(result);
    }

    @Override
    public PageResult<PointsTransactionEntity> pageAll(String type, int page, int size) {
        IPage<PointsTransactionPO> result = transactionMapper.selectPageAll(
                new Page<>(page, size), type);
        return toPageResult(result);
    }

    @Override
    public Long sumGrantByMonth(int year, int month) {
        return transactionMapper.sumAmountByTypeAndMonth("grant", year, month);
    }

    @Override
    public Long sumDeductByMonth(int year, int month) {
        return transactionMapper.sumAmountByTypeAndMonth("deduct", year, month);
    }

    private PageResult<PointsTransactionEntity> toPageResult(IPage<PointsTransactionPO> result) {
        PageResult<PointsTransactionEntity> pageResult = new PageResult<>();
        pageResult.setCurrent(result.getCurrent());
        pageResult.setSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setPages(result.getPages());
        pageResult.setRecords(result.getRecords().stream()
                .map(this::toEntity)
                .collect(Collectors.toList()));
        return pageResult;
    }

    private PointsTransactionEntity toEntity(PointsTransactionPO po) {
        PointsTransactionEntity entity = new PointsTransactionEntity();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setType(po.getType());
        entity.setAmount(po.getAmount());
        entity.setReason(po.getReason());
        entity.setOrderId(po.getOrderId());
        entity.setCreatedAt(po.getCreatedAt());
        return entity;
    }

    private PointsTransactionPO toPO(PointsTransactionEntity entity) {
        PointsTransactionPO po = new PointsTransactionPO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setType(entity.getType());
        po.setAmount(entity.getAmount());
        po.setReason(entity.getReason());
        po.setOrderId(entity.getOrderId());
        po.setCreatedAt(entity.getCreatedAt());
        return po;
    }
}
