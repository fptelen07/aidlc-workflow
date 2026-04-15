# Component Inventory

## Application Packages

| Package | Module Count | Type | Purpose |
|---------|-------------|------|---------|
| awsome-shop-gateway-service | 16 Maven modules | API Gateway | Spring Cloud Gateway WebFlux |
| awsome-shop-auth-service | 16 Maven modules | Backend Service | Authentication & Authorization |
| awsome-shop-product-service | 16 Maven modules | Backend Service | Product Catalog Management |
| awsome-shop-order-service | 16 Maven modules | Backend Service | Order/Redemption Processing |
| awsome-shop-points-service | 16 Maven modules | Backend Service | Points Management |
| awsome-shop-frontend | npm | Frontend | React SPA |

**Total: 5 backend services × 16 modules + 1 frontend = 81 modules**

## Shared Module Structure (Per Backend Service)

Each backend service follows a consistent 16-module DDD layered architecture:

| Layer | Module | Purpose |
|-------|--------|---------|
| Root | root pom | Parent POM, dependency management |
| Shared | common | Cross-cutting utilities |
| Domain | domain-model | Entities, value objects, aggregates |
| Domain | domain-api | Domain service interfaces |
| Domain | domain-impl | Domain service implementations |
| Domain | repository-api | Repository interfaces |
| Domain | cache-api | Cache abstraction interfaces |
| Domain | mq-api | Message queue abstraction interfaces |
| Domain | security-api | Security abstraction interfaces |
| Infrastructure | mysql-impl | MySQL/MyBatis-Plus repository implementations |
| Infrastructure | redis-impl | Redis cache implementations |
| Infrastructure | sqs-impl | AWS SQS message queue implementations |
| Infrastructure | jwt-impl | JWT security implementations |
| Application | application-api | Application service interfaces |
| Application | application-impl | Application service implementations |
| Interface | interface-http | REST API controllers |
| Interface | interface-consumer | Message queue consumers |
| Deployment | bootstrap | Spring Boot application entry point |
