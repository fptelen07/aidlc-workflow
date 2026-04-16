package com.awsome.shop.point.domain.impl.service.points;

import com.awsome.shop.point.common.dto.PageResult;
import com.awsome.shop.point.common.enums.PointsErrorCode;
import com.awsome.shop.point.common.exception.BusinessException;
import com.awsome.shop.point.domain.model.points.PointsAccountEntity;
import com.awsome.shop.point.domain.model.points.PointsTransactionEntity;
import com.awsome.shop.point.domain.service.points.PointsDomainService;
import com.awsome.shop.point.repository.points.PointsAccountRepository;
import com.awsome.shop.point.repository.points.PointsTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 积分领域服务实现
 */
@Service
@RequiredArgsConstructor
public class PointsDomainServiceImpl implements PointsDomainService {

    private final PointsAccountRepository accountRepository;
    private final PointsTransactionRepository transactionRepository;

    @Override
    public Long getBalance(Long userId) {
        PointsAccountEntity account = accountRepository.findByUserId(userId);
        if (account == null) {
            return 0L;
        }
        return account.getBalance();
    }

    @Override
    @Transactional
    public void grant(Long userId, Long amount, String reason) {
        if (amount == null || amount <= 0) {
            throw new BusinessException(PointsErrorCode.INVALID_AMOUNT);
        }
        ensureAccountExists(userId);
        accountRepository.updateBalance(userId, amount);
        recordTransaction(userId, "grant", amount, reason, null);
    }

    @Override
    @Transactional
    public void deduct(Long userId, Long amount, String reason, Long orderId) {
        if (amount == null || amount <= 0) {
            throw new BusinessException(PointsErrorCode.INVALID_AMOUNT);
        }
        PointsAccountEntity account = accountRepository.findByUserId(userId);
        if (account == null || account.getBalance() < amount) {
            throw new BusinessException(PointsErrorCode.INSUFFICIENT_BALANCE);
        }
        accountRepository.updateBalance(userId, -amount);
        recordTransaction(userId, "deduct", amount, reason, orderId);
    }

    @Override
    @Transactional
    public void batchGrant(List<Long> userIds, Long amount, String reason) {
        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException(PointsErrorCode.INVALID_USER_IDS);
        }
        if (amount == null || amount <= 0) {
            throw new BusinessException(PointsErrorCode.INVALID_AMOUNT);
        }
        for (Long userId : userIds) {
            ensureAccountExists(userId);
            accountRepository.updateBalance(userId, amount);
            recordTransaction(userId, "grant", amount, reason, null);
        }
    }

    @Override
    public PageResult<PointsTransactionEntity> getHistory(Long userId, String type, int page, int size) {
        return transactionRepository.pageByUserId(userId, type, page, size);
    }

    @Override
    public PageResult<PointsTransactionEntity> getAllHistory(String type, int page, int size) {
        return transactionRepository.pageAll(type, page, size);
    }

    @Override
    public Long sumGrantByMonth(int year, int month) {
        return transactionRepository.sumGrantByMonth(year, month);
    }

    @Override
    public Long sumDeductByMonth(int year, int month) {
        return transactionRepository.sumDeductByMonth(year, month);
    }

    private void ensureAccountExists(Long userId) {
        PointsAccountEntity account = accountRepository.findByUserId(userId);
        if (account == null) {
            PointsAccountEntity newAccount = new PointsAccountEntity();
            newAccount.setUserId(userId);
            newAccount.setBalance(0L);
            accountRepository.save(newAccount);
        }
    }

    private void recordTransaction(Long userId, String type, Long amount, String reason, Long orderId) {
        PointsTransactionEntity tx = new PointsTransactionEntity();
        tx.setUserId(userId);
        tx.setType(type);
        tx.setAmount(amount);
        tx.setReason(reason);
        tx.setOrderId(orderId);
        tx.setCreatedAt(LocalDateTime.now());
        transactionRepository.save(tx);
    }
}
