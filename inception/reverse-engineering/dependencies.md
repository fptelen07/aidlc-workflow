# Dependencies Analysis

## Internal Dependencies

```mermaid
graph TD
    FE[Frontend - React SPA] -->|HTTP API| GW[Gateway Service :8080]
    GW -->|Token Validation via WebClient| AUTH[Auth Service :8081]
    GW -->|Request Routing| PROD[Product Service :8082]
    GW -->|Request Routing| ORD[Order Service :8083]
    GW -->|Request Routing| PTS[Points Service :8084]
    ORD -.->|Verify Product - Planned| PROD
    ORD -.->|Deduct Points - Planned| PTS
    AUTH --> MySQL[(MySQL - auth_db)]
    PROD --> MySQL2[(MySQL - product_db)]
    ORD --> MySQL3[(MySQL - order_db)]
    PTS --> MySQL4[(MySQL - points_db)]
    AUTH --> REDIS[(Redis - Shared Cache)]
    PROD --> REDIS
    ORD --> REDIS
    PTS --> REDIS
    ORD -.->|Planned Async Events| SQS[AWS SQS]
    PTS -.->|Planned Async Events| SQS

    style FE fill:#61dafb,color:#000
    style GW fill:#6db33f,color:#fff
    style AUTH fill:#6db33f,color:#fff
    style PROD fill:#6db33f,color:#fff
    style ORD fill:#6db33f,color:#fff
    style PTS fill:#6db33f,color:#fff
    style SQS fill:#ff9900,color:#000
```

**Legend**: Solid lines = implemented, Dashed lines = planned/not yet implemented

## External Dependencies

### Backend (Java/Spring Boot)

| Dependency | Purpose |
|---|---|
| Spring Boot 3.x | Application framework |
| Spring Cloud Gateway | API gateway (reactive) |
| MyBatis-Plus | ORM / data access |
| Druid | Connection pooling |
| MySQL Connector | Database driver |
| Lettuce Redis | Redis client (reactive-compatible) |
| AWS SQS SDK | Async messaging (planned) |
| jjwt | JWT library (declared, unused) |
| Flyway | Database migration |
| Lombok | Boilerplate reduction |
| SpringDoc | OpenAPI/Swagger documentation |
| Micrometer | Metrics/observability |
| Logstash Logback Encoder | Structured logging |
| JaCoCo | Code coverage reporting |
| TestContainers | Integration test infrastructure |

### Frontend (TypeScript/React)

| Dependency | Purpose |
|---|---|
| React | UI framework |
| MUI (Material UI) | Component library |
| Zustand | State management |
| Axios | HTTP client |
| i18next | Internationalization |
| Vite | Build tool / dev server |
