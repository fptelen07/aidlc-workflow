# Technology Stack

## Backend

| Category | Technology | Version |
|----------|-----------|---------|
| Language | Java | 21 |
| Framework | Spring Boot | 3.4.1 (auth/product/order/points), 3.5.10 (gateway) |
| Cloud | Spring Cloud | 2025.0.0 |
| API Gateway | Spring Cloud Gateway | WebFlux |
| ORM | MyBatis-Plus | 3.5.7 |
| Connection Pool | Druid | 1.2.20 |
| Database | MySQL | 8.4 |
| Cache | Redis (Lettuce) | — |
| Messaging | AWS SQS SDK | 2.20.0 |
| Security | jjwt | 0.12.6 |
| Migration | Flyway | — |
| Code Gen | Lombok | 1.18.36 |
| API Docs | SpringDoc OpenAPI | 2.7.0 |
| Tracing | Micrometer Tracing + Brave | — |
| Metrics | Prometheus | — |
| Logging | Logstash Logback Encoder | 7.4 |
| Coverage | JaCoCo | — |
| Testing | TestContainers (MySQL, Redis) | — |

## Frontend

| Category | Technology | Version |
|----------|-----------|---------|
| UI Library | React | 19.2 |
| Language | TypeScript | 5.9 |
| Build Tool | Vite | 7.3 |
| Component Library | MUI (Material-UI) | 6.5 |
| CSS-in-JS | Emotion | 11.14 |
| State Management | Zustand | 5.0 |
| Routing | React Router | 7.13 |
| i18n | i18next | 25.8 |
| i18n React | react-i18next | 16.5 |
| HTTP Client | Axios | 1.13 |
| Linting | ESLint | 9.39 |
