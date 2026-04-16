package com.awsome.shop.point.application.api.service.points;

import com.awsome.shop.point.application.api.dto.points.BalanceDTO;
import com.awsome.shop.point.application.api.dto.points.StatisticsDTO;
import com.awsome.shop.point.application.api.dto.points.TransactionDTO;
import com.awsome.shop.point.application.api.dto.points.request.*;
import com.awsome.shop.point.common.dto.PageResult;

/**
 * 积分应用服务接口
 */
public interface PointsApplicationService {

    BalanceDTO getBalance(GetBalanceRequest request);

    void grant(GrantPointsRequest request);

    void batchGrant(BatchGrantRequest request);

    void deduct(DeductPointsRequest request);

    PageResult<TransactionDTO> getMyHistory(GetMyHistoryRequest request);

    PageResult<TransactionDTO> getAllHistory(GetAllHistoryRequest request);

    StatisticsDTO getStatistics(GetStatisticsRequest request);
}
