# Code Structure - awsome-shop

## 1. Build System

### Backend - Multi-Module Maven

Each backend service (auth, product, order, points, gateway) is an independent Maven multi-module project with identical module layout:

```
awsome-shop-{service}-service/
├── pom.xml                    # Parent POM with dependency management
├── common/                    # Shared DTOs, exceptions, enums, annotations
├── domain/                    # Domain layer (DDD)
│   ├── domain-model/          # Entities, value objects
│   ├── domain-api/            # Domain service interfaces
│   ├── domain-impl/           # Domain service implementations
│   ├── repository-api/        # Repository port interfaces
│   ├── security-api/          # Security port interfaces
│   ├── cache-api/             # Cache port interfaces (EMPTY)
│   └── mq-api/                # Message queue port interfaces (EMPTY)
├── application/               # Application layer
│   ├── application-api/       # Application service interfaces + DTOs
│   └── application-impl/      # Application service implementations
├── infrastructure/            # Infrastructure adapters
│   ├── repository/
│   │   └── mysql-impl/        # MyBatis-Plus repository implementations
│   ├── cache/
│   │   └── redis-impl/        # Redis cache adapter
│   ├── security/
│   │   └── jwt-impl/          # AES encryption adapter
│   └── mq/
│       └── sqs-impl/          # SQS adapter (EMPTY)
├── interface/                 # Interface layer
│   ├── interface-http/        # REST controllers, request/response DTOs
│   └── interface-consumer/    # Message consumer handlers
└── bootstrap/                 # Spring Boot application entry point + configs
```

**Build commands**: `mvn clean install` per service (no shared parent across services).

### Frontend - Vite + npm

```
awsome-shop-frontend/
├── package.json               # npm project with Vite 7.3.1
├── vite.config.ts             # Vite configuration
├── tsconfig.json              # TypeScript project references
├── tsconfig.app.json          # App-specific TS config
├── tsconfig.node.json         # Node-specific TS config
├── eslint.config.js           # ESLint flat config
├── index.html                 # SPA entry point
└── src/                       # Application source
```

**Build commands**: `npm run dev` (development), `npm run build` (production: `tsc -b && vite build`).

---

## 2. Key Classes/Modules Hierarchy

### Backend Services (auth, product, order, points) - Shared Structure

Each service uses package root `com.awsome.shop.{service}`:

```
common/
├── exception/
│   ├── BaseException              # Abstract base, holds ErrorCode
│   ├── BusinessException          # Business rule violations
│   ├── SystemException            # Infrastructure/system failures
│   └── ParameterException         # Input validation failures
├── enums/
│   ├── ErrorCode                  # Interface: code(), message(), httpStatus()
│   ├── ParamErrorCode             # Enum implementing ErrorCode (P_ prefix → 400)
│   ├── SystemErrorCode            # Enum implementing ErrorCode (S_ prefix → 500)
│   └── SampleErrorCode            # Placeholder enum (PLACEHOLDER)
├── dto/
│   └── PageResult<T>              # Paginated response wrapper
├── result/
│   └── Result<T>                  # Unified API response wrapper
└── annotation/
    └── RequireOwnerPermission     # Resource ownership authorization

domain/
├── domain-model/
│   └── model/test/TestEntity      # Placeholder domain entity
├── domain-api/
│   └── service/test/TestDomainService        # Placeholder domain service interface
├── domain-impl/
│   └── service/test/TestDomainServiceImpl    # Placeholder domain service impl
├── repository-api/
│   └── test/TestRepository                   # Placeholder repository port
├── security-api/
│   └── service/EncryptionService             # Encryption port interface
├── cache-api/                                # EMPTY module
└── mq-api/                                   # EMPTY module

application/
├── application-api/
│   ├── service/test/TestApplicationService   # Placeholder app service interface
│   └── dto/test/
│       ├── TestDTO                           # Placeholder DTO
│       └── request/
│           ├── CreateTestRequest             # Placeholder
│           ├── UpdateTestRequest             # Placeholder
│           ├── GetTestRequest                # Placeholder
│           ├── DeleteTestRequest             # Placeholder
│           └── ListTestRequest               # Placeholder
└── application-impl/
    └── service/test/TestApplicationServiceImpl  # Placeholder app service impl

infrastructure/
├── repository/mysql-impl/
│   ├── config/
│   │   ├── MybatisPlusConfig                 # MyBatis-Plus pagination + optimistic locking
│   │   ├── CustomMetaObjectHandler           # Auto-fill created/updated timestamps + operator
│   │   └── UserContext                       # ThreadLocal holder for current user ID
│   ├── impl/test/TestRepositoryImpl          # Placeholder repository adapter
│   ├── mapper/test/TestMapper                # Placeholder MyBatis mapper interface
│   └── po/test/TestPO                        # Placeholder persistence object
├── cache/redis-impl/
│   └── config/RedisConfig                    # Redis connection configuration
└── security/jwt-impl/
    └── crypto/AesEncryptionServiceImpl       # AES encryption adapter

interface/
├── interface-http/
│   ├── controller/TestController             # Placeholder REST controller
│   ├── request/common/
│   │   ├── GatewayInjectableRequest          # Base request with gateway-injected operatorId
│   │   └── PageableRequest                   # Base request with pagination params
│   ├── response/
│   │   ├── Result<T>                         # HTTP response wrapper
│   │   └── ErrorDetail                       # Error response detail
│   └── exception/
│       └── GlobalExceptionHandler            # @RestControllerAdvice exception handler
└── interface-consumer/
    └── exception/GlobalConsumerExceptionHandler  # MQ consumer exception handler

bootstrap/
├── Application                               # @SpringBootApplication entry point
└── config/
    ├── OpenApiConfig                         # Swagger/OpenAPI configuration
    └── AsyncConfig                           # Async thread pool configuration
```

### Gateway Service - Additional Classes (19 gateway-specific files)

Beyond the shared 42-file scaffold, the gateway adds:

```
common/
├── constants/RouteConstants                  # Route path constants
├── dto/ErrorResponse                         # Gateway error response DTO
├── enums/GatewayErrorCode                    # Gateway-specific error codes (G_ prefix)
└── exception/
    ├── GatewayException                      # Base gateway exception
    ├── AuthenticationException               # Auth failure exception
    └── ServiceUnavailableException           # Downstream service unavailable

domain/
├── domain-model/auth/
│   ├── TokenInfo                             # JWT token parsed info
│   └── AuthenticationResult                  # Auth validation result
└── domain-api/auth/
    └── AuthenticationService                 # Auth domain service interface

application/
└── application-api/auth/dto/
    ├── AuthValidateRequest                   # Auth validation request DTO
    └── AuthValidateResponse                  # Auth validation response DTO

infrastructure/gateway/gateway-impl/
├── config/
│   ├── JacksonConfig                         # Jackson ObjectMapper configuration
│   ├── WebClientConfig                       # WebClient for downstream calls
│   └── GlobalExceptionHandler                # Reactive WebFlux exception handler
├── auth/client/
│   └── AuthServiceClient                     # WebClient-based auth service client
└── filter/
    ├── AccessLogFilter                       # Request/response access logging
    ├── AuthenticationGatewayFilter            # JWT token validation filter
    ├── OperatorIdInjectionFilter             # Injects operatorId header to downstream
    └── SwaggerServersRewriteGatewayFilterFactory  # Swagger URL rewrite filter

bootstrap/config/
├── SwaggerConfig                             # Gateway Swagger aggregation config
└── CorsConfig                                # CORS configuration
```

### Frontend - Module Hierarchy

```
src/
├── main.tsx                                  # React DOM root mount
├── App.tsx                                   # MUI ThemeProvider + i18n + RouterProvider
├── theme/index.ts                            # MUI theme customization
├── i18n/
│   ├── index.ts                              # i18next initialization + language detection
│   └── locales/
│       ├── en.json                           # English translations
│       └── zh.json                           # Chinese translations
├── router/
│   ├── index.tsx                             # Route definitions (createBrowserRouter)
│   └── AuthGuard.tsx                         # Auth-protected route wrapper
├── store/
│   ├── useAuthStore.ts                       # Zustand auth state (token, user, persist)
│   └── useAppStore.ts                        # Zustand app state (sidebar, language)
├── services/
│   └── request.ts                            # Axios instance with interceptors
├── components/
│   ├── AvatarMenu.tsx                        # User avatar dropdown menu
│   └── Layout/
│       ├── EmployeeLayout.tsx                # Employee role layout shell
│       ├── AdminLayout.tsx                   # Admin role layout shell
│       ├── index.tsx                         # Generic layout (UNUSED)
│       ├── AppHeader.tsx                     # App header component (UNUSED)
│       └── Sidebar.tsx                       # Sidebar navigation (UNUSED)
└── pages/
    ├── Login/index.tsx                       # Login page with form
    ├── ShopHome/index.tsx                    # Shop home page (employee view)
    ├── Dashboard/index.tsx                   # Admin dashboard page
    ├── NotFound/index.tsx                    # 404 page
    └── Home/index.tsx                        # Generic home page (UNUSED)
```

---

## 3. Existing Files Inventory

### 3.1 Auth Service (`awsome-shop-auth-service`) — 42 Java files

#### Framework Code (~23 files)

| File | Module | Purpose |
|------|--------|---------|
| `BaseException.java` | common | Abstract exception base class with ErrorCode |
| `BusinessException.java` | common | Business rule violation exception |
| `SystemException.java` | common | System/infrastructure failure exception |
| `ParameterException.java` | common | Input validation failure exception |
| `ErrorCode.java` | common | Interface defining code(), message(), httpStatus() |
| `ParamErrorCode.java` | common | Parameter error codes enum (P_ prefix → 400) |
| `SystemErrorCode.java` | common | System error codes enum (S_ prefix → 500) |
| `Result.java` | common | Unified API response wrapper DTO |
| `PageResult.java` | common | Paginated response wrapper DTO |
| `RequireOwnerPermission.java` | common | Annotation for resource ownership checks |
| `GatewayInjectableRequest.java` | interface-http | Base request with gateway-injected operatorId |
| `PageableRequest.java` | interface-http | Base request with pagination parameters |
| `Result.java` | interface-http | HTTP-layer response wrapper |
| `ErrorDetail.java` | interface-http | Error response detail DTO |
| `GlobalExceptionHandler.java` | interface-http | @RestControllerAdvice exception handler |
| `GlobalConsumerExceptionHandler.java` | interface-consumer | MQ consumer exception handler |
| `MybatisPlusConfig.java` | mysql-impl | Pagination interceptor + optimistic locking |
| `CustomMetaObjectHandler.java` | mysql-impl | Auto-fill timestamps and operator fields |
| `UserContext.java` | mysql-impl | ThreadLocal current user ID holder |
| `RedisConfig.java` | redis-impl | Redis connection configuration |
| `AesEncryptionServiceImpl.java` | jwt-impl | AES encryption service adapter |
| `EncryptionService.java` | security-api | Encryption port interface |
| `Application.java` | bootstrap | @SpringBootApplication entry point |
| `OpenApiConfig.java` | bootstrap | Swagger/OpenAPI configuration |
| `AsyncConfig.java` | bootstrap | Async thread pool configuration |

#### Placeholder Test CRUD (~19 files)

| File | Module | Purpose |
|------|--------|---------|
| `TestEntity.java` | domain-model | Placeholder domain entity |
| `TestDomainService.java` | domain-api | Placeholder domain service interface |
| `TestDomainServiceImpl.java` | domain-impl | Placeholder domain service implementation |
| `TestRepository.java` | repository-api | Placeholder repository port interface |
| `TestRepositoryImpl.java` | mysql-impl | Placeholder repository adapter |
| `TestApplicationService.java` | application-api | Placeholder application service interface |
| `TestApplicationServiceImpl.java` | application-impl | Placeholder application service implementation |
| `TestDTO.java` | application-api | Placeholder data transfer object |
| `CreateTestRequest.java` | application-api | Placeholder create request DTO |
| `UpdateTestRequest.java` | application-api | Placeholder update request DTO |
| `GetTestRequest.java` | application-api | Placeholder get request DTO |
| `DeleteTestRequest.java` | application-api | Placeholder delete request DTO |
| `ListTestRequest.java` | application-api | Placeholder list request DTO |
| `TestController.java` | interface-http | Placeholder REST controller |
| `TestMapper.java` | mysql-impl | Placeholder MyBatis mapper interface |
| `TestMapper.xml` | mysql-impl/resources | Placeholder MyBatis XML mapping |
| `TestPO.java` | mysql-impl | Placeholder persistence object |
| `V1__create_test_table.sql` | bootstrap/resources | Flyway migration for test table |
| `SampleErrorCode.java` | common | Placeholder error code enum |

#### Configuration Files

| File | Purpose |
|------|---------|
| `application.yml` | Base Spring Boot configuration |
| `application-local.yml` | Local development profile |
| `application-dev.yml` | Development environment profile |
| `application-test.yml` | Test environment profile |
| `application-staging.yml` | Staging environment profile |
| `application-prod.yml` | Production environment profile |
| `application-docker.yml` | Docker environment profile |
| `logback-spring.xml` | Logback logging configuration |

#### Empty Modules

| Module | Purpose |
|--------|---------|
| `cache-api` | Cache port interfaces (no Java files) |
| `mq-api` | Message queue port interfaces (no Java files) |
| `sqs-impl` | SQS adapter implementation (no Java files) |

### 3.2 Product Service (`awsome-shop-product-service`) — 42 Java files

Identical structure to Auth Service. Package root: `com.awsome.shop.product`. Same 23 framework files + 19 placeholder test CRUD files. Same configuration files and empty modules.

### 3.3 Order Service (`awsome-shop-order-service`) — 42 Java files

Identical structure to Auth Service. Package root: `com.awsome.shop.order`. Same 23 framework files + 19 placeholder test CRUD files. Same configuration files and empty modules.

### 3.4 Points Service (`awsome-shop-points-service`) — 42 Java files

Identical structure to Auth Service. Package root: `com.awsome.shop.point`. Same 23 framework files + 19 placeholder test CRUD files. Same configuration files and empty modules.

### 3.5 Gateway Service (`awsome-shop-gateway-service`) — 61 Java files

42 shared scaffold files (same as other services, package root: `com.awsome.shop.gateway`) plus 19 gateway-specific files:

#### Gateway-Specific Files (19 files)

| File | Module | Purpose |
|------|--------|---------|
| `TokenInfo.java` | domain-model | JWT token parsed information model |
| `AuthenticationResult.java` | domain-model | Authentication validation result model |
| `AuthenticationService.java` | domain-api | Authentication domain service interface |
| `AuthValidateRequest.java` | application-api | Auth validation request DTO |
| `AuthValidateResponse.java` | application-api | Auth validation response DTO |
| `AuthServiceClient.java` | gateway-impl | WebClient-based auth service HTTP client |
| `AccessLogFilter.java` | gateway-impl | Request/response access logging filter |
| `AuthenticationGatewayFilter.java` | gateway-impl | JWT token validation gateway filter |
| `OperatorIdInjectionFilter.java` | gateway-impl | Injects operatorId header to downstream services |
| `SwaggerServersRewriteGatewayFilterFactory.java` | gateway-impl | Rewrites Swagger server URLs through gateway |
| `JacksonConfig.java` | gateway-impl | Jackson ObjectMapper configuration |
| `WebClientConfig.java` | gateway-impl | WebClient bean configuration |
| `GlobalExceptionHandler.java` | gateway-impl | Reactive WebFlux global exception handler |
| `CorsConfig.java` | bootstrap | CORS configuration |
| `SwaggerConfig.java` | bootstrap | Gateway Swagger aggregation configuration |
| `GatewayErrorCode.java` | common | Gateway-specific error codes (G_ prefix) |
| `GatewayException.java` | common | Base gateway exception |
| `AuthenticationException.java` | common | Authentication failure exception |
| `ServiceUnavailableException.java` | common | Downstream service unavailable exception |
| `ErrorResponse.java` | common | Gateway error response DTO |
| `RouteConstants.java` | common | Route path constants |

#### Gateway Configuration Files (additional)

| File | Purpose |
|------|---------|
| `application.yml` | Gateway routing configuration with Spring Cloud Gateway routes |

### 3.6 Frontend (`awsome-shop-frontend`) — 20 TypeScript/TSX source files

#### Implemented Files (16 files)

| File | Purpose |
|------|---------|
| `src/main.tsx` | React DOM root mount point |
| `src/App.tsx` | Root component: MUI ThemeProvider + i18n + RouterProvider |
| `src/theme/index.ts` | MUI theme customization (palette, typography) |
| `src/i18n/index.ts` | i18next initialization with browser language detection |
| `src/i18n/locales/en.json` | English translation strings |
| `src/i18n/locales/zh.json` | Chinese translation strings |
| `src/router/index.tsx` | Route definitions using createBrowserRouter |
| `src/router/AuthGuard.tsx` | Auth-protected route wrapper (redirects to /login) |
| `src/store/useAuthStore.ts` | Zustand auth store (token, user, login/logout, persist) |
| `src/store/useAppStore.ts` | Zustand app store (sidebar collapsed, language) |
| `src/services/request.ts` | Axios instance with auth token interceptor |
| `src/components/AvatarMenu.tsx` | User avatar dropdown with logout/language switch |
| `src/components/Layout/EmployeeLayout.tsx` | Employee role layout shell with sidebar + header |
| `src/components/Layout/AdminLayout.tsx` | Admin role layout shell with sidebar + header |
| `src/pages/Login/index.tsx` | Login page with username/password form |
| `src/pages/ShopHome/index.tsx` | Shop home page (employee product browsing view) |
| `src/pages/Dashboard/index.tsx` | Admin dashboard with statistics cards |
| `src/pages/NotFound/index.tsx` | 404 not found page |

#### Unused Files (4 files)

| File | Purpose |
|------|---------|
| `src/components/Layout/index.tsx` | Generic layout component (not referenced in routes) |
| `src/components/Layout/AppHeader.tsx` | App header component (not referenced in routes) |
| `src/components/Layout/Sidebar.tsx` | Sidebar navigation component (not referenced in routes) |
| `src/pages/Home/index.tsx` | Generic home page (not referenced in routes) |

#### Frontend Configuration Files

| File | Purpose |
|------|---------|
| `package.json` | npm dependencies and scripts |
| `vite.config.ts` | Vite build configuration |
| `tsconfig.json` | TypeScript project references |
| `tsconfig.app.json` | App TypeScript configuration |
| `tsconfig.node.json` | Node TypeScript configuration |
| `eslint.config.js` | ESLint flat configuration |
| `index.html` | SPA HTML entry point |

---

## 4. Design Patterns

### Domain-Driven Design (DDD)

- **Domain Model**: Entities in `domain-model/` (currently only placeholder `TestEntity`)
- **Domain Service**: Business logic interfaces in `domain-api/`, implementations in `domain-impl/`
- **Repository Pattern**: Port interfaces in `repository-api/`, adapters in `mysql-impl/`
- **Application Service**: Orchestration layer in `application-api/` and `application-impl/`
- **Layered separation**: domain → application → interface, with infrastructure as adapters

### Hexagonal Architecture (Ports and Adapters)

- **Ports (interfaces)**: `domain/repository-api/`, `domain/security-api/`, `domain/cache-api/`, `domain/mq-api/`
- **Adapters (implementations)**: `infrastructure/repository/mysql-impl/`, `infrastructure/cache/redis-impl/`, `infrastructure/security/jwt-impl/`, `infrastructure/mq/sqs-impl/`
- **Dependency inversion**: Domain layer defines interfaces; infrastructure layer provides implementations

### POST-Only API Design

- All API endpoints use HTTP POST method exclusively
- Request bodies carry all parameters (no path variables or query params for business operations)

### Gateway-Injectable Request Pattern

- `GatewayInjectableRequest` base class contains `operatorId` field
- Gateway's `OperatorIdInjectionFilter` extracts user ID from JWT and injects it as HTTP header
- Downstream services receive operator identity without direct JWT parsing

### Error Code Prefix to HTTP Status Mapping

- Error code prefixes determine HTTP status: `P_` → 400 (Bad Request), `S_` → 500 (Internal Server Error), `G_` → gateway-specific
- `ErrorCode` interface provides `httpStatus()` method for automatic mapping

### PO-Entity-DTO Conversion at Layer Boundaries

- **PO (Persistence Object)**: Database table mapping in `mysql-impl/po/`
- **Entity**: Domain model in `domain-model/`
- **DTO**: Data transfer in `application-api/dto/`
- Conversions happen at layer boundaries to maintain separation

### Logical Delete + Optimistic Locking

- `MybatisPlusConfig` configures `OptimisticLockerInnerInterceptor`
- `CustomMetaObjectHandler` auto-fills `createdAt`, `updatedAt`, `createdBy`, `updatedBy`
- Logical delete via MyBatis-Plus `@TableLogic` annotation support

### ThreadLocal UserContext

- `UserContext` uses `ThreadLocal<Long>` to store current operator ID
- Set from gateway-injected header at request entry
- Used by `CustomMetaObjectHandler` for audit field population

### Gateway Filter Chain

- **AccessLogFilter**: Logs request/response details
- **AuthenticationGatewayFilter**: Validates JWT tokens via auth service
- **OperatorIdInjectionFilter**: Injects operator ID header for downstream services
- **SwaggerServersRewriteGatewayFilterFactory**: Rewrites Swagger server URLs

### Frontend State Management - Zustand with Persist Middleware

- `useAuthStore`: Persisted auth state (token, user info) using Zustand persist middleware
- `useAppStore`: App-level state (sidebar collapsed, language preference)
- Persist middleware stores state in localStorage for session continuity

---

## 5. Critical Dependencies with Versions

### Backend Dependencies

| Dependency | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Runtime and compilation target |
| Spring Boot | 3.4.1 (services) / 3.5.10 (gateway) | Application framework |
| Spring Cloud | 2025.0.0 | Cloud-native features (Gateway) |
| Spring Cloud Gateway | (via Spring Cloud BOM) | API gateway routing and filtering |
| MyBatis-Plus | 3.5.7 | ORM framework with code generation |
| Druid | 1.2.20 | Database connection pooling |
| Lombok | 1.18.36 | Boilerplate code reduction |
| JJWT | 0.12.6 | JWT token creation and validation |
| AWS SDK (SQS) | 2.20.0 | Amazon SQS messaging (placeholder) |
| Micrometer Tracing | 1.3.5 | Distributed tracing |
| Logstash Logback Encoder | 7.4 | Structured JSON logging |
| JaCoCo | 0.8.12 | Code coverage reporting |
| Flyway | (via Spring Boot) | Database schema migration |
| SpringDoc OpenAPI | (via Spring Boot) | API documentation |

### Frontend Dependencies

| Dependency | Version | Purpose |
|-----------|---------|---------|
| React | ^19.2.0 | UI framework |
| React DOM | ^19.2.0 | React DOM renderer |
| React Router | ^7.13.0 | Client-side routing |
| MUI Material | ^6.5.0 | UI component library |
| MUI Icons Material | ^6.5.0 | Material Design icons |
| Emotion React | ^11.14.0 | CSS-in-JS (MUI styling engine) |
| Emotion Styled | ^11.14.1 | Styled components (MUI styling engine) |
| Axios | ^1.13.5 | HTTP client |
| Zustand | ^5.0.11 | State management |
| i18next | ^25.8.4 | Internationalization framework |
| react-i18next | ^16.5.4 | React i18n bindings |
| i18next-browser-languagedetector | ^8.2.0 | Browser language detection |
| TypeScript | ~5.9.3 | Type-safe JavaScript |
| Vite | ^7.3.1 | Build tool and dev server |
| ESLint | ^9.39.1 | Code linting |

### Notable Version Discrepancy

- **Spring Boot**: Backend services use `3.4.1`, while Gateway uses `3.5.10`. This is intentional — the gateway requires Spring Cloud Gateway which needs the reactive stack from a newer Spring Boot version.
