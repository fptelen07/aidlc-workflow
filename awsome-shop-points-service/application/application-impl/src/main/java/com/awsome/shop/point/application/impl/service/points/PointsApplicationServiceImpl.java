package com.awsome.shop.point.application.impl.service.points;

import com.awsome.shop.point.application.api.dto.points.BalanceDTO;
import com.awsome.shop.point.application.api.dto.points.StatisticsDTO;
import com.awsome.shop.point.application.api.dto.points.TransactionDTO;
import com.awsome.shop.point.application.api.dto.points.request.*;
import com.awsome.shop.point.application.api.service.points.PointsApplicationService;
import com.awsome.shop.point.common.dto.PageResult;
import com.awsome.shop.point.domain.model.points.PointsTransactionEntity;
import com.awsome.shop.point.domain.service.points.PointsDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 积分应用服务实现
 */
@Service
@RequiredArgsConstructor
public class PointsApplicationServiceImpl implements PointsApplicationService {

    private final PointsDomainService pointsDomainService;

    @Override
    public BalanceDTO getBalance(GetBalanceRequest request) {
        Long balance = pointsDomainService.getBalance(request.getOperatorId());
        BalanceDTO dto = new BalanceDTO();
        dto.setUserId(request.getOperatorId());
        dto.setBalance(balance);
        return dto;
    }

    @Override
    public void grant(GrantPointsRequest request) {
        pointsDomainService.grant(request.getUserId(), request.getAmount(), request.getReason());
    }

    @Override
    public void batchGrant(BatchGrantRequest request) {
        pointsDomainService.batchGrant(request.getUserIds(), request.getAmount(), request.getReason());
    }

    @Override
    public void deduct(DeductPointsRequest request) {
        pointsDomainService.deduct(request.getUserId(), request.getAmount(),
                request.getReason(), request.getOrderId());
    }

    @Override
    public PageResult<TransactionDTO> getMyHistory(GetMyHistoryRequest request) {
        PageResult<PointsTransactionEntity> page = pointsDomainService.getHistory(
                request.getOperatorId(), request.getType(), request.getPage(), request.getSize());
        return page.convert(this::toDTO);
    }

    @Override
    public PageResult<TransactionDTO> getAllHistory(GetAllHistoryRequest request) {
        PageResult<PointsTransactionEntity> page = pointsDomainService.getAllHistory(
                request.getType(), request.getPage(), request.getSize());
        return page.convert(this::toDTO);
    }

    @Override
    public StatisticsDTO getStatistics(GetStatisticsRequest request) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        Long granted = pointsDomainService.sumGrantByMonth(year, month);
        Long deducted = pointsDomainService.sumDeductByMonth(year, month);

        StatisticsDTO dto = new StatisticsDTO();
        dto.setMonthlyGranted(granted != null ? granted : 0L);
        dto.setMonthlyDeducted(deducted != null ? deducted : 0L);
        dto.setMonthlyNet(dto.getMonthlyGranted() - dto.getMonthlyDeducted());
        return dto;
    }

    private TransactionDTO toDTO(PointsTransactionEntity entity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());
        dto.setReason(entity.getReason());
        dto.setOrderId(entity.getOrderId());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
