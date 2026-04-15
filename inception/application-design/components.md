# AWSome Shop - Component Definitions

## Architecture Overview

All backend services follow DDD hexagonal architecture with these layers:
- **domain-model**: Entities, value objects, enums
- **domain-api**: Domain service interfaces
- **domain-impl**: Domain service implementations
- **repository-api**: Repository interfaces
- **cache-api**: Cache abstractions
- **mq-api**: Message queue abstractions
- **security-api**: Security abstractions (token, password)
- **application-api**: Application service interfaces
- **application-impl**: Application service implementations
- **interface-http**: REST controllers (POST-only API)
- **interface-consumer**: MQ consumers
- **bootstrap**: Spring Boot entry point, configuration

**API Design**: All endpoints use POST method. The API gateway injects `operatorId` into the request body before forwarding to downstream services.

---

## 1. Auth Service

### 1.1 User

- **Purpose**: Core identity entity representing a system user
- **Responsibilities**:
  - Hold user identity attributes (id, username, passwordHash, displayName, avatar)
  - Track user role (EMPLOYEE or ADMIN) and status
  - Record creation and update timestamps
- **Layer**: Domain Model

### 1.2 Role

- **Purpose**: Enum defining user authorization levels
- **Responsibilities**:
  - Define EMPLOYEE and ADMIN roles
- **Layer**: Domain Model

### 1.3 UserDomainService

- **Purpose**: Core domain logic for user lifecycle
- **Responsibilities**:
  - Register new users with uniqueness validation
  - Authenticate users by username and password
  - Find users by ID or username
- **Layer**: Domain API / Domain Impl

### 1.4 PasswordService

- **Purpose**: Domain abstraction for password hashing
- **Responsibilities**:
  - Hash plaintext passwords
  - Verify passwords against stored hashes
- **Layer**: Domain API / Domain Impl

### 1.5 UserRepository

- **Purpose**: Persistence abstraction for User aggregate
- **Responsibilities**:
  - Save user entities
  - Find users by ID or username
  - Check username existence
- **Layer**: Repository API (Infrastructure)

### 1.6 TokenService

- **Purpose**: Security abstraction for JWT token management
- **Responsibilities**:
  - Generate access tokens
  - Generate refresh tokens
  - Validate tokens
  - Extract userId from tokens
- **Layer**: Security API (Infrastructure)

### 1.7 PasswordHashService

- **Purpose**: Infrastructure implementation of password hashing
- **Responsibilities**:
  - Hash passwords using BCrypt or similar
  - Verify password against hash
- **Layer**: Security API (Infrastructure)

### 1.8 AuthApplicationService

- **Purpose**: Orchestrate authentication and authorization use cases
- **Responsibilities**:
  - Login (authenticate + generate tokens)
  - Register (create user + generate tokens)
  - Refresh token
  - Validate token (for gateway/internal calls)
  - Get current user profile
- **Layer**: Application API / Application Impl

### 1.9 AuthController

- **Purpose**: HTTP interface for auth operations
- **Responsibilities**:
  - Public endpoints: login, register, refreshToken
  - Internal endpoint: validate (called by gateway)
  - Protected endpoint: getCurrentUser
- **Layer**: Interface HTTP

---

## 2. Product Service

### 2.1 Product

- **Purpose**: Core entity representing a redeemable product
- **Responsibilities**:
  - Hold product attributes (id, name, description, categoryId, pointsPrice, stockQuantity, imageUrl)
  - Track product status (ON_SHELF / OFF_SHELF)
  - Record creation and update timestamps
- **Layer**: Domain Model

### 2.2 Category

- **Purpose**: Product classification entity
- **Responsibilities**:
  - Hold category attributes (id, name, sortOrder)
  - Record creation and update timestamps
- **Layer**: Domain Model

### 2.3 ProductStatus

- **Purpose**: Enum defining product availability states
- **Responsibilities**:
  - Define ON_SHELF and OFF_SHELF states
- **Layer**: Domain Model

### 2.4 ProductDomainService

- **Purpose**: Core domain logic for product lifecycle
- **Responsibilities**:
  - Create, update, delete products
  - Find product by ID, list by filter with pagination
  - Update product status (on/off shelf)
  - Deduct stock quantity with concurrency safety
- **Layer**: Domain API / Domain Impl

### 2.5 CategoryDomainService

- **Purpose**: Core domain logic for category management
- **Responsibilities**:
  - Create, update, delete categories
  - Find all categories, find by ID
- **Layer**: Domain API / Domain Impl

### 2.6 ProductRepository

- **Purpose**: Persistence abstraction for Product aggregate
- **Responsibilities**:
  - Save, update, delete products
  - Find by ID, paginated query
  - Update product status
  - Deduct stock (atomic operation)
- **Layer**: Repository API (Infrastructure)

### 2.7 CategoryRepository

- **Purpose**: Persistence abstraction for Category aggregate
- **Responsibilities**:
  - Save, update, delete categories
  - Find by ID, find all
  - Count products by category ID (for delete validation)
- **Layer**: Repository API (Infrastructure)

### 2.8 ProductApplicationService

- **Purpose**: Orchestrate product use cases
- **Responsibilities**:
  - CRUD operations for products
  - List products with filtering and pagination
  - Toggle product status (on/off shelf)
  - Deduct stock (called by Order Service)
- **Layer**: Application API / Application Impl

### 2.9 CategoryApplicationService

- **Purpose**: Orchestrate category use cases
- **Responsibilities**:
  - CRUD operations for categories
  - List all categories
- **Layer**: Application API / Application Impl

### 2.10 ProductController

- **Purpose**: HTTP interface for product operations
- **Responsibilities**:
  - Public endpoints: list products, get product detail
  - Protected endpoints: create, update, delete, toggleStatus, deductStock
- **Layer**: Interface HTTP

### 2.11 CategoryController

- **Purpose**: HTTP interface for category operations
- **Responsibilities**:
  - Public endpoint: list categories
  - Protected endpoints: create, update, delete
- **Layer**: Interface HTTP

---

## 3. Order Service

### 3.1 Order

- **Purpose**: Core entity representing a points redemption order
- **Responsibilities**:
  - Hold order attributes (id, userId, userName, productId, productName, pointsAmount)
  - Track order status (PENDING, COMPLETED, REJECTED)
  - Record creation and update timestamps
- **Layer**: Domain Model

### 3.2 OrderStatus

- **Purpose**: Enum defining order lifecycle states
- **Responsibilities**:
  - Define PENDING, COMPLETED, REJECTED states
- **Layer**: Domain Model

### 3.3 OrderDomainService

- **Purpose**: Core domain logic for order lifecycle
- **Responsibilities**:
  - Create orders with initial PENDING status
  - Confirm orders (transition to COMPLETED)
  - Reject orders (transition to REJECTED)
  - Find order by ID
  - List orders by user, list all orders
- **Layer**: Domain API / Domain Impl

### 3.4 OrderRepository

- **Purpose**: Persistence abstraction for Order aggregate
- **Responsibilities**:
  - Save, update orders
  - Find by ID
  - Paginated query by userId
  - Paginated query for all orders
- **Layer**: Repository API (Infrastructure)

### 3.5 OrderApplicationService

- **Purpose**: Orchestrate order use cases with cross-service coordination
- **Responsibilities**:
  - Create order (verify product via Product Service, deduct points via Points Service)
  - Confirm order (admin approval)
  - Reject order (admin rejection, refund points)
  - Get single order
  - List current user's orders
  - List all orders (admin)
- **Layer**: Application API / Application Impl

### 3.6 OrderController

- **Purpose**: HTTP interface for order operations
- **Responsibilities**:
  - Protected endpoints: create order, getMyOrders, getOrder
  - Admin endpoints: listAll, confirm, reject
- **Layer**: Interface HTTP

### 3.7 Cross-Service Dependencies

- **Product Service**: Verify product exists, is on-shelf, and has stock
- **Points Service**: Deduct points on order creation, refund on rejection

---

## 4. Points Service

### 4.1 PointsAccount

- **Purpose**: Entity representing a user's points balance
- **Responsibilities**:
  - Hold account attributes (id, userId, balance)
  - Record creation and update timestamps
- **Layer**: Domain Model

### 4.2 PointsTransaction

- **Purpose**: Entity recording every points movement
- **Responsibilities**:
  - Hold transaction attributes (id, userId, amount, type, reason, relatedOrderId)
  - Record creation timestamp
- **Layer**: Domain Model

### 4.3 TransactionType

- **Purpose**: Enum defining points movement types
- **Responsibilities**:
  - Define GRANT, DEDUCT, EXPIRE types
- **Layer**: Domain Model

### 4.4 PointsRule

- **Purpose**: Entity defining automated points granting rules
- **Responsibilities**:
  - Hold rule attributes (id, name, type, amount, enabled)
  - Record creation and update timestamps
- **Layer**: Domain Model

### 4.5 PointsDomainService

- **Purpose**: Core domain logic for points operations
- **Responsibilities**:
  - Get user balance
  - Grant points (with transaction record)
  - Deduct points (with balance validation and transaction record)
  - Get transaction history
  - Batch grant points to multiple users
- **Layer**: Domain API / Domain Impl

### 4.6 PointsRuleDomainService

- **Purpose**: Core domain logic for points rule management
- **Responsibilities**:
  - Create, update rules
  - Toggle rule enabled status
  - Find all rules
- **Layer**: Domain API / Domain Impl

### 4.7 PointsExpirationService

- **Purpose**: Domain service for points expiration processing
- **Responsibilities**:
  - Process expired points (scheduled)
- **Layer**: Domain API / Domain Impl

### 4.8 PointsAccountRepository

- **Purpose**: Persistence abstraction for PointsAccount
- **Responsibilities**:
  - Save accounts
  - Find by userId
  - Update balance (atomic operation)
- **Layer**: Repository API (Infrastructure)

### 4.9 PointsTransactionRepository

- **Purpose**: Persistence abstraction for PointsTransaction
- **Responsibilities**:
  - Save transactions
  - Paginated query by userId
  - Paginated query for all transactions
  - Get aggregated statistics
- **Layer**: Repository API (Infrastructure)

### 4.10 PointsRuleRepository

- **Purpose**: Persistence abstraction for PointsRule
- **Responsibilities**:
  - Save, update rules
  - Find all, find by ID
- **Layer**: Repository API (Infrastructure)

### 4.11 PointsApplicationService

- **Purpose**: Orchestrate points use cases
- **Responsibilities**:
  - Get balance for current user
  - Grant points to user (admin)
  - Batch grant points (admin)
  - Deduct points (called by Order Service)
  - Get current user's transaction history
  - Get all transaction history (admin)
  - Get points statistics (admin)
- **Layer**: Application API / Application Impl

### 4.12 PointsRuleApplicationService

- **Purpose**: Orchestrate points rule use cases
- **Responsibilities**:
  - CRUD operations for rules
  - Toggle rule enabled status
- **Layer**: Application API / Application Impl

### 4.13 PointsController

- **Purpose**: HTTP interface for points operations
- **Responsibilities**:
  - Protected endpoints: getBalance, getMyHistory
  - Admin endpoints: grant, batchGrant, getAllHistory, getStatistics
- **Layer**: Interface HTTP

### 4.14 PointsRuleController

- **Purpose**: HTTP interface for points rule management
- **Responsibilities**:
  - Admin endpoints: create, update, delete, list, toggleEnabled
- **Layer**: Interface HTTP

---

## 5. Frontend (Vue 3 + TypeScript)

### 5.1 API Services

| Service | Purpose | Layer |
|---------|---------|-------|
| authService | Login, register, refresh token, get current user | Infrastructure |
| productService | Product CRUD, list, status toggle, stock deduction | Infrastructure |
| categoryService | Category CRUD, list | Infrastructure |
| orderService | Order creation, listing, confirmation, rejection | Infrastructure |
| pointsService | Balance, history, grant, deduct, statistics, rules | Infrastructure |
| userService | User profile operations | Infrastructure |
| dashboardService | Admin dashboard aggregated data | Infrastructure |

### 5.2 Pages

| Page | Purpose | Access | Layer |
|------|---------|--------|-------|
| Register | New user registration | Public | Interface |
| ProductDetail | View product details and redeem | Employee | Interface |
| EmployeeOrders | View personal order history | Employee | Interface |
| EmployeePoints | View personal points balance and history | Employee | Interface |
| AdminProducts | Manage products (CRUD, status toggle) | Admin | Interface |
| AdminCategories | Manage categories (CRUD) | Admin | Interface |
| AdminPoints | Grant points, view history, statistics, manage rules | Admin | Interface |
| AdminOrders | View all orders, confirm/reject | Admin | Interface |
| AdminUsers | View and manage users | Admin | Interface |

### 5.3 State Management

- **useAuthStore**: Extend with real API login/register, token persistence, auto-refresh, user profile caching
- **Layer**: Application
