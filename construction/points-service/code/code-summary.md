# Points Service — Code Summary (Unit 4)

## Overview
积分服务 (Points Service) 完整实现，管理用户积分账户、交易记录和积分规则配置。

## 变更清单

### 领域模型 (domain-model)
| 文件 | 说明 |
|------|------|
| `model/points/PointsAccountEntity.java` | 积分账户实体 (userId, balance, createdAt, updatedAt) |
| `model/points/PointsTransactionEntity.java` | 积分交易实体 (id, userId, type, amount, reason, orderId, createdAt) |
| `model/points/PointsRuleEntity.java` | 积分规则实体 (id, name, type, amount, enabled, description) |

### 错误码 (common)
| 文件 | 说明 |
|------|------|
| `enums/PointsErrorCode.java` | 积分服务错误码 (ACCOUNT_NOT_FOUND, RULE_NOT_FOUND, INSUFFICIENT_BALANCE, INVALID_AMOUNT, INVALID_USER_IDS) |

### 仓储接口 (repository-api)
| 文件 | 说明 |
|------|------|
| `repository/points/PointsAccountRepository.java` | 积分账户仓储 (findByUserId, save, updateBalance) |
| `repository/points/PointsTransactionRepository.java` | 积分交易仓储 (save, pageByUserId, pageAll, sumGrantByMonth, sumDeductByMonth) |
| `repository/points/PointsRuleRepository.java` | 积分规则仓储 (save, update, findById, findAll, updateEnabled) |

### 领域服务接口 (domain-api)
| 文件 | 说明 |
|------|------|
| `service/points/PointsDomainService.java` | 积分领域服务 (getBalance, grant, deduct, batchGrant, getHistory, getAllHistory, sumGrantByMonth, sumDeductByMonth) |
| `service/points/PointsRuleDomainService.java` | 积分规则领域服务 (create, update, toggleEnabled, findAll) |
| `service/points/PointsExpirationService.java` | 积分过期服务 (processExpiredPoints) |

### 领域服务实现 (domain-impl)
| 文件 | 说明 |
|------|------|
| `service/points/PointsDomainServiceImpl.java` | 积分领域服务实现，含事务管理、余额校验、自动创建账户 |
| `service/points/PointsRuleDomainServiceImpl.java` | 积分规则领域服务实现 |

### 应用层 DTO (application-api)
| 文件 | 说明 |
|------|------|
| `dto/points/BalanceDTO.java` | 积分余额响应 |
| `dto/points/TransactionDTO.java` | 积分交易响应 |
| `dto/points/StatisticsDTO.java` | 积分统计响应 (monthlyGranted, monthlyDeducted, monthlyNet) |
| `dto/points/PointsRuleDTO.java` | 积分规则响应 |
| `dto/points/request/GetBalanceRequest.java` | 查询余额请求 |
| `dto/points/request/GrantPointsRequest.java` | 发放积分请求 |
| `dto/points/request/BatchGrantRequest.java` | 批量发放请求 |
| `dto/points/request/DeductPointsRequest.java` | 扣减积分请求 |
| `dto/points/request/GetMyHistoryRequest.java` | 查询我的历史请求 (含分页) |
| `dto/points/request/GetAllHistoryRequest.java` | 查询所有历史请求 (含分页) |
| `dto/points/request/GetStatisticsRequest.java` | 查询统计请求 |
| `dto/points/request/CreateRuleRequest.java` | 创建规则请求 |
| `dto/points/request/UpdateRuleRequest.java` | 更新规则请求 |
| `dto/points/request/ToggleRuleRequest.java` | 切换规则启用状态请求 |

### 应用服务 (application-api / application-impl)
| 文件 | 说明 |
|------|------|
| `service/points/PointsApplicationService.java` | 积分应用服务接口 |
| `service/points/PointsRuleApplicationService.java` | 积分规则应用服务接口 |
| `service/points/PointsApplicationServiceImpl.java` | 积分应用服务实现 |
| `service/points/PointsRuleApplicationServiceImpl.java` | 积分规则应用服务实现 |

### 基础设施 — MySQL (mysql-impl)
| 文件 | 说明 |
|------|------|
| `po/points/PointsAccountPO.java` | 积分账户持久化对象 |
| `po/points/PointsTransactionPO.java` | 积分交易持久化对象 |
| `po/points/PointsRulePO.java` | 积分规则持久化对象 |
| `mapper/points/PointsAccountMapper.java` | 积分账户 Mapper |
| `mapper/points/PointsTransactionMapper.java` | 积分交易 Mapper |
| `mapper/points/PointsRuleMapper.java` | 积分规则 Mapper |
| `resources/mapper/points/PointsAccountMapper.xml` | 账户 Mapper XML (updateBalance) |
| `resources/mapper/points/PointsTransactionMapper.xml` | 交易 Mapper XML (分页查询, 月度统计) |
| `resources/mapper/points/PointsRuleMapper.xml` | 规则 Mapper XML |
| `impl/points/PointsAccountRepositoryImpl.java` | 积分账户仓储实现 |
| `impl/points/PointsTransactionRepositoryImpl.java` | 积分交易仓储实现 |
| `impl/points/PointsRuleRepositoryImpl.java` | 积分规则仓储实现 |

### Controller (interface-http)
| 文件 | 说明 |
|------|------|
| `controller/PointsController.java` | 积分管理 7 个端点 (balance, my-history, grant, batch-grant, deduct, history, statistics) |
| `controller/PointsRuleController.java` | 积分规则管理 4 个端点 (create, update, toggle, list) |

### Flyway 迁移 (bootstrap)
| 文件 | 说明 |
|------|------|
| `V2__create_points_tables.sql` | 创建 points_account, points_transaction, points_rule 三张表 |
| `V3__seed_points_data.sql` | 种子数据：示例员工账户 (userId=2, balance=2580)、管理员账户、交易记录、规则 |

## API 端点

| 端点 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/v1/point/balance` | POST | 查询积分余额 | protected |
| `/api/v1/point/my-history` | POST | 查询我的积分历史 | protected |
| `/api/v1/point/grant` | POST | 发放积分 | protected, admin |
| `/api/v1/point/batch-grant` | POST | 批量发放积分 | protected, admin |
| `/api/v1/point/deduct` | POST | 扣减积分 | protected |
| `/api/v1/point/history` | POST | 查询所有积分历史 | protected, admin |
| `/api/v1/point/statistics` | POST | 积分统计 | protected, admin |
| `/api/v1/point/rule/create` | POST | 创建积分规则 | protected, admin |
| `/api/v1/point/rule/update` | POST | 更新积分规则 | protected, admin |
| `/api/v1/point/rule/toggle` | POST | 切换规则启用状态 | protected, admin |
| `/api/v1/point/rule/list` | POST | 查询所有积分规则 | protected, admin |

## 编译验证
EC2 (Java 21) 编译通过 ✅
