# Order Service — Code Summary

## 变更概览
实现 Unit 5 (订单服务) 的完整业务代码，覆盖 DDD 六边形架构的所有层。

## 新增文件清单

### Common 层
| 文件 | 说明 |
|------|------|
| `common/.../enums/OrderErrorCode.java` | 订单错误码：ORDER_NOT_FOUND, INVALID_STATUS, INSUFFICIENT_STOCK, INSUFFICIENT_POINTS |

### Domain 层
| 文件 | 说明 |
|------|------|
| `domain/domain-model/.../model/order/OrderEntity.java` | 订单领域实体 |
| `domain/domain-api/.../service/order/OrderDomainService.java` | 领域服务接口（create, confirm, reject, findById, listByUser, listAll） |
| `domain/repository-api/.../repository/order/OrderRepository.java` | 仓储接口 |
| `domain/domain-impl/.../service/order/OrderDomainServiceImpl.java` | 领域服务实现（含状态流转校验） |

### Application 层
| 文件 | 说明 |
|------|------|
| `application/application-api/.../dto/order/OrderDTO.java` | 订单 DTO |
| `application/application-api/.../dto/order/request/CreateOrderRequest.java` | 创建订单请求 |
| `application/application-api/.../dto/order/request/ConfirmOrderRequest.java` | 确认订单请求 |
| `application/application-api/.../dto/order/request/RejectOrderRequest.java` | 拒绝订单请求 |
| `application/application-api/.../dto/order/request/GetOrderRequest.java` | 获取订单请求 |
| `application/application-api/.../dto/order/request/ListMyOrdersRequest.java` | 我的订单列表请求 |
| `application/application-api/.../dto/order/request/ListAllOrdersRequest.java` | 全部订单列表请求 |
| `application/application-api/.../service/order/OrderApplicationService.java` | 应用服务接口 |
| `application/application-impl/.../service/order/OrderApplicationServiceImpl.java` | 应用服务实现（含 TODO 跨服务调用标记） |

### Infrastructure 层
| 文件 | 说明 |
|------|------|
| `infrastructure/repository/mysql-impl/.../po/order/OrderPO.java` | 持久化对象（表名 redemption_order） |
| `infrastructure/repository/mysql-impl/.../mapper/order/OrderMapper.java` | MyBatis Mapper 接口 |
| `infrastructure/repository/mysql-impl/.../impl/order/OrderRepositoryImpl.java` | 仓储实现 |
| `infrastructure/repository/mysql-impl/src/main/resources/mapper/order/OrderMapper.xml` | Mapper XML（分页查询） |

### Interface 层
| 文件 | 说明 |
|------|------|
| `interface/interface-http/.../controller/OrderController.java` | REST Controller（6 个端点） |

### Flyway 迁移
| 文件 | 说明 |
|------|------|
| `bootstrap/src/main/resources/db/migration/V2__create_order_table.sql` | 创建 redemption_order 表 |

## API 端点
| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/v1/order/create` | 创建订单 | protected |
| POST | `/api/v1/order/get` | 获取订单详情 | protected |
| POST | `/api/v1/order/my-list` | 我的订单列表 | protected |
| POST | `/api/v1/order/list` | 全部订单列表 | protected, admin |
| POST | `/api/v1/order/confirm` | 确认订单 | protected, admin |
| POST | `/api/v1/order/reject` | 拒绝订单 | protected, admin |

## 订单状态流转
```
pending → completed  (管理员确认)
pending → rejected   (管理员拒绝)
```

## 待集成项（TODO）
- `createOrder`: 调用 Product Service 获取商品信息，调用 Points Service 验证余额
- `confirmOrder`: 调用 Points Service 扣减积分，调用 Product Service 扣减库存，失败补偿
- `createOrder`: 调用 Auth Service 获取用户名

## 编译验证
EC2 编译通过 ✅
