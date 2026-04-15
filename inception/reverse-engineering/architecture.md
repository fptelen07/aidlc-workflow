# System Architecture

## System Overview

AWSome Shop 是一个基于微服务架构的员工积分兑换商城。系统采用前后端分离架构，后端由 5 个 Spring Boot 微服务组成，通过 API Gateway 统一对外暴露。每个微服务遵循 DDD + 六边形架构，使用多模块 Maven 项目组织代码。

## Architecture Diagram

```mermaid
graph TB
    Browser["Browser<br/>React SPA"]
    
    subgraph Gateway["API Gateway :8080"]
        AccessLog["AccessLogFilter"]
        AuthFilter["AuthenticationFilter"]
        OpIdFilter["OperatorIdInjectionFilter"]
        SwaggerFilter["SwaggerRewriteFilter"]
    end
    
    subgraph Services["Backend Microservices"]
        AuthSvc["Auth Service :8001"]
        ProductSvc["Product Service :8002"]
        PointsSvc["Points Service :8003"]
        OrderSvc["Order Service :8004"]
    end
    
    subgraph DataStores["Data Stores"]
        AuthDB["MySQL<br/>awsome_shop_auth"]
        ProductDB["MySQL<br/>awsome_shop_product"]
        PointsDB["MySQL<br/>awsome_shop_point"]
        OrderDB["MySQL<br/>awsome_shop_order"]
        Redis["Redis<br/>Cache/Session"]
        SQS["AWS SQS<br/>Message Queue"]
    end
    
    Browser -->|HTTP| Gateway
    Gateway -->|Route| AuthSvc
    Gateway -->|Route| ProductSvc
    Gateway -->|Route| PointsSvc
    Gateway -->|Route| OrderSvc
    
    AuthSvc --> AuthDB
    AuthSvc --> Redis
    ProductSvc --> ProductDB
    ProductSvc --> Redis
    PointsSvc --> PointsDB
    PointsSvc --> Redis
    OrderSvc --> OrderDB
    OrderSvc --> Redis
    
    OrderSvc -.->|Event| SQS
    PointsSvc -.->|Event| SQS
```

## Component Descriptions

### API Gateway (awsome-shop-gateway-service)
- **Purpose**: 统一 API 入口，请求路由与认证
- **Responsibilities**: JWT 认证、operatorId 注入、请求路由、Swagger 聚合、访问日志
- **Dependencies**: Auth Service (token 验证)
- **Type**: Application (Spring Cloud Gateway, WebFlux)

### Auth Service (awsome-shop-auth-service)
- **Purpose**: 用户认证与授权
- **Responsibilities**: 登录、注册、JWT 生成/验证、密码加密
- **Dependencies**: MySQL, Redis
- **Type**: Application (Spring Boot, Servlet)

### Product Service (awsome-shop-product-service)
- **Purpose**: 商品与分类管理
- **Responsibilities**: 商品 CRUD、分类 CRUD、搜索筛选
- **Dependencies**: MySQL, Redis
- **Type**: Application (Spring Boot, Servlet)

### Order Service (awsome-shop-order-service)
- **Purpose**: 兑换订单处理
- **Responsibilities**: 订单创建、状态流转、订单查询
- **Dependencies**: MySQL, Redis, SQS (planned)
- **Type**: Application (Spring Boot, Servlet)

### Points Service (awsome-shop-points-service)
- **Purpose**: 积分管理
- **Responsibilities**: 积分余额、发放、扣减、历史、统计
- **Dependencies**: MySQL, Redis, SQS (planned)
- **Type**: Application (Spring Boot, Servlet)

### Frontend SPA (awsome-shop-frontend)
- **Purpose**: 用户界面
- **Responsibilities**: 员工端商城、管理端后台、国际化、主题切换
- **Dependencies**: API Gateway
- **Type**: Application (React SPA)

## Data Flow

### 兑换流程 (Redemption Flow)

```mermaid
sequenceDiagram
    participant E as Employee
    participant FE as Frontend
    participant GW as Gateway
    participant PS as Product Service
    participant OS as Order Service
    participant PTS as Points Service
    
    E->>FE: Click Redeem
    FE->>GW: POST /api/v1/order/create (Bearer token)
    GW->>GW: Validate JWT, inject operatorId
    GW->>OS: Forward request
    OS->>PS: Verify product availability
    PS-->>OS: Product info
    OS->>PTS: Deduct points
    PTS-->>OS: Points deducted
    OS-->>GW: Order created
    GW-->>FE: Response
    FE-->>E: Show success
```

### 认证流程 (Authentication Flow)

```mermaid
sequenceDiagram
    participant U as User
    participant FE as Frontend
    participant GW as Gateway
    participant AS as Auth Service
    
    U->>FE: Login (username, password)
    FE->>GW: POST /api/v1/public/auth/login
    GW->>GW: Skip auth (public path)
    GW->>AS: Forward request
    AS->>AS: Validate credentials, generate JWT
    AS-->>GW: Token + UserInfo
    GW-->>FE: Response
    FE->>FE: Store token in localStorage
    
    Note over FE,GW: Subsequent requests
    FE->>GW: Request with Bearer token
    GW->>AS: POST /api/v1/internal/auth/validate
    AS-->>GW: operatorId
    GW->>GW: Inject operatorId into request body
    GW->>GW: Route to target service
```

## Integration Points

- **External APIs**: None (self-contained system)
- **Databases**: MySQL 8.4 (per-service database isolation)
- **Cache**: Redis (shared instance, per-service key namespace)
- **Message Queue**: AWS SQS (planned, for inter-service async events)
- **API Documentation**: SpringDoc OpenAPI, aggregated via Gateway Swagger UI

## Infrastructure Components

- **Gateway**: Spring Cloud Gateway (WebFlux, reactive)
- **Service Discovery**: Direct URL routing (no Eureka/Consul)
- **Deployment Model**: Docker Compose (local), per-service JAR
- **Database Migration**: Flyway (per-service)
- **Monitoring**: Micrometer Tracing + Prometheus metrics + Actuator endpoints
- **Logging**: Logstash Logback Encoder (JSON format for non-local profiles)
