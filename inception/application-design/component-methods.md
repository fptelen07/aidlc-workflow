# Component Methods

> Detailed business rules (validation, error handling, edge cases) will be defined in Functional Design.

---

## Auth Service

### UserDomainService (Domain)
- `register(username: String, passwordHash: String, displayName: String): User` — Create and persist a new user
- `authenticate(username: String, password: String): User` — Verify credentials and return user
- `findById(id: Long): User` — Retrieve user by ID
- `findByUsername(username: String): User` — Retrieve user by username

### TokenService (Security API)
- `generateAccessToken(userId: Long, role: String): String` — Issue JWT access token
- `generateRefreshToken(userId: Long): String` — Issue JWT refresh token
- `validateToken(token: String): TokenClaims` — Validate and decode token
- `extractUserId(token: String): Long` — Extract user ID from token

### PasswordHashService (Security API)
- `hash(rawPassword: String): String` — Hash a raw password
- `verify(rawPassword: String, hashedPassword: String): boolean` — Verify password against hash

### UserRepository (Infrastructure)
- `save(user: User): User` — Persist a new user
- `findById(id: Long): User` — Find user by ID
- `findByUsername(username: String): User` — Find user by username
- `existsByUsername(username: String): boolean` — Check if username is taken

### AuthApplicationService (Application)
- `login(req: LoginRequest): LoginResponse` — Authenticate and return tokens
- `register(req: RegisterRequest): void` — Register a new user account
- `refreshToken(req: RefreshTokenRequest): TokenResponse` — Refresh access token
- `validateToken(req: ValidateRequest): ValidateResponse` — Validate token for gateway
- `getCurrentUser(userId: Long): UserDTO` — Get current user profile; operatorId from gateway

### AuthController (API)
- `POST /api/v1/public/auth/login` — User login (public)
- `POST /api/v1/public/auth/register` — User registration (public)
- `POST /api/v1/public/auth/refresh` — Refresh token (public)
- `POST /api/v1/internal/auth/validate` — Token validation (internal)
- `POST /api/v1/auth/me` — Get current user (protected; operatorId in body)

---

## Product Service

### ProductDomainService (Domain)
- `create(product: Product): Product` — Create a new product
- `update(product: Product): Product` — Update product details
- `delete(id: Long): void` — Delete a product
- `findById(id: Long): Product` — Find product by ID
- `listByFilter(categoryId: Long, name: String, status: String, page: int, size: int): PageResult<Product>` — Paginated filtered product list
- `updateStatus(id: Long, status: String): void` — Enable/disable a product
- `deductStock(id: Long, quantity: int): void` — Deduct product stock

### CategoryDomainService (Domain)
- `create(category: Category): Category` — Create a new category
- `update(category: Category): Category` — Update category details
- `delete(id: Long): void` — Delete a category
- `findAll(): List<Category>` — List all categories
- `findById(id: Long): Category` — Find category by ID

### ProductRepository (Infrastructure)
- `save(product: Product): Product` — Persist a new product
- `update(product: Product): Product` — Update existing product
- `findById(id: Long): Product` — Find product by ID
- `page(filter: ProductFilter): PageResult<Product>` — Paginated query with filter
- `deleteById(id: Long): void` — Delete product by ID
- `updateStatus(id: Long, status: String): void` — Update product status
- `deductStock(id: Long, quantity: int): void` — Deduct stock atomically

### CategoryRepository (Infrastructure)
- `save(category: Category): Category` — Persist a new category
- `update(category: Category): Category` — Update existing category
- `findById(id: Long): Category` — Find category by ID
- `findAll(): List<Category>` — List all categories
- `deleteById(id: Long): void` — Delete category by ID
- `countProductsByCategoryId(categoryId: Long): int` — Count products in category

### ProductApplicationService (Application)
- `createProduct(req: CreateProductRequest): ProductDTO` — Create product; operatorId in req
- `updateProduct(req: UpdateProductRequest): ProductDTO` — Update product; operatorId in req
- `deleteProduct(req: DeleteProductRequest): void` — Delete product; operatorId in req
- `getProduct(req: GetProductRequest): ProductDTO` — Get product detail (public)
- `listProducts(req: ListProductsRequest): PageResult<ProductDTO>` — List products with filter (public); extends PageableRequest
- `toggleStatus(req: ToggleStatusRequest): void` — Toggle product status; operatorId in req
- `deductStock(req: DeductStockRequest): void` — Deduct stock; operatorId in req

### CategoryApplicationService (Application)
- `createCategory(req: CreateCategoryRequest): CategoryDTO` — Create category; operatorId in req
- `updateCategory(req: UpdateCategoryRequest): CategoryDTO` — Update category; operatorId in req
- `deleteCategory(req: DeleteCategoryRequest): void` — Delete category; operatorId in req
- `listCategories(): List<CategoryDTO>` — List all categories (public)

### ProductController (API)
- `POST /api/v1/public/product/list` — List products with filter (public)
- `POST /api/v1/public/product/get` — Get product detail (public)
- `POST /api/v1/product/create` — Create product (protected)
- `POST /api/v1/product/update` — Update product (protected)
- `POST /api/v1/product/delete` — Delete product (protected)
- `POST /api/v1/product/toggle-status` — Toggle product status (protected)
- `POST /api/v1/product/deduct-stock` — Deduct product stock (protected)

### CategoryController (API)
- `POST /api/v1/public/category/list` — List all categories (public)
- `POST /api/v1/category/create` — Create category (protected)
- `POST /api/v1/category/update` — Update category (protected)
- `POST /api/v1/category/delete` — Delete category (protected)

---

## Order Service

### OrderDomainService (Domain)
- `create(order: Order): Order` — Create a new order
- `confirm(id: Long): Order` — Confirm a pending order
- `reject(id: Long): Order` — Reject a pending order
- `findById(id: Long): Order` — Find order by ID
- `listByUser(userId: Long, status: String, page: int, size: int): PageResult<Order>` — Paginated orders for a user
- `listAll(status: String, userName: String, page: int, size: int): PageResult<Order>` — Paginated all orders (admin)

### OrderRepository (Infrastructure)
- `save(order: Order): Order` — Persist a new order
- `update(order: Order): Order` — Update existing order
- `findById(id: Long): Order` — Find order by ID
- `pageByUserId(userId: Long, status: String, page: int, size: int): PageResult<Order>` — Paginated query by user
- `pageAll(status: String, userName: String, page: int, size: int): PageResult<Order>` — Paginated query for all orders

### OrderApplicationService (Application)
- `createOrder(req: CreateOrderRequest): OrderDTO` — Create order; operatorId in req
- `confirmOrder(req: ConfirmOrderRequest): OrderDTO` — Confirm order; operatorId in req
- `rejectOrder(req: RejectOrderRequest): OrderDTO` — Reject order; operatorId in req
- `getOrder(req: GetOrderRequest): OrderDTO` — Get order detail; operatorId in req
- `listMyOrders(req: ListMyOrdersRequest): PageResult<OrderDTO>` — List current user's orders; operatorId in req; extends PageableRequest
- `listAllOrders(req: ListAllOrdersRequest): PageResult<OrderDTO>` — List all orders (admin); operatorId in req; extends PageableRequest

### OrderController (API)
- `POST /api/v1/order/create` — Create order (protected)
- `POST /api/v1/order/get` — Get order detail (protected)
- `POST /api/v1/order/my-list` — List my orders (protected)
- `POST /api/v1/order/list` — List all orders (protected, admin)
- `POST /api/v1/order/confirm` — Confirm order (protected, admin)
- `POST /api/v1/order/reject` — Reject order (protected, admin)

---

## Points Service

### PointsDomainService (Domain)
- `getBalance(userId: Long): Long` — Get user's points balance
- `grant(userId: Long, amount: Long, reason: String): void` — Grant points to a user
- `deduct(userId: Long, amount: Long, reason: String, orderId: Long): void` — Deduct points for an order
- `batchGrant(userIds: List<Long>, amount: Long, reason: String): void` — Grant points to multiple users
- `getHistory(userId: Long, type: String, page: int, size: int): PageResult<PointsTransaction>` — Paginated transaction history

### PointsRuleDomainService (Domain)
- `create(rule: PointsRule): PointsRule` — Create a points rule
- `update(rule: PointsRule): PointsRule` — Update a points rule
- `toggleEnabled(id: Long): void` — Enable/disable a rule
- `findAll(): List<PointsRule>` — List all rules

### PointsExpirationService (Domain)
- `processExpiredPoints(): void` — Expire outdated points (scheduled)

### PointsAccountRepository (Infrastructure)
- `findByUserId(userId: Long): PointsAccount` — Find account by user ID
- `save(account: PointsAccount): PointsAccount` — Persist account
- `updateBalance(userId: Long, amount: Long): void` — Update balance atomically

### PointsTransactionRepository (Infrastructure)
- `save(tx: PointsTransaction): PointsTransaction` — Persist transaction
- `pageByUserId(userId: Long, type: String, page: int, size: int): PageResult<PointsTransaction>` — Paginated by user
- `pageAll(type: String, page: int, size: int): PageResult<PointsTransaction>` — Paginated all transactions

### PointsRuleRepository (Infrastructure)
- `save(rule: PointsRule): PointsRule` — Persist rule
- `update(rule: PointsRule): PointsRule` — Update rule
- `findById(id: Long): PointsRule` — Find rule by ID
- `findAll(): List<PointsRule>` — List all rules
- `updateEnabled(id: Long, enabled: boolean): void` — Toggle rule enabled status

### PointsApplicationService (Application)
- `getBalance(req: GetBalanceRequest): BalanceDTO` — Get balance; operatorId in req
- `grant(req: GrantPointsRequest): void` — Grant points; operatorId in req
- `batchGrant(req: BatchGrantRequest): void` — Batch grant points; operatorId in req
- `deduct(req: DeductPointsRequest): void` — Deduct points; operatorId in req
- `getMyHistory(req: GetMyHistoryRequest): PageResult<TransactionDTO>` — Current user's history; operatorId in req; extends PageableRequest
- `getAllHistory(req: GetAllHistoryRequest): PageResult<TransactionDTO>` — All history (admin); operatorId in req; extends PageableRequest
- `getStatistics(req: GetStatisticsRequest): StatisticsDTO` — Points statistics; operatorId in req

### PointsRuleApplicationService (Application)
- `createRule(req: CreateRuleRequest): PointsRuleDTO` — Create rule; operatorId in req
- `updateRule(req: UpdateRuleRequest): PointsRuleDTO` — Update rule; operatorId in req
- `toggleRule(req: ToggleRuleRequest): void` — Toggle rule; operatorId in req
- `listRules(): List<PointsRuleDTO>` — List all rules

### PointsController (API)
- `POST /api/v1/point/balance` — Get points balance (protected)
- `POST /api/v1/point/my-history` — Get my transaction history (protected)
- `POST /api/v1/point/grant` — Grant points (protected, admin)
- `POST /api/v1/point/batch-grant` — Batch grant points (protected, admin)
- `POST /api/v1/point/deduct` — Deduct points (protected)
- `POST /api/v1/point/history` — Get all history (protected, admin)
- `POST /api/v1/point/statistics` — Get statistics (protected, admin)

### PointsRuleController (API)
- `POST /api/v1/point/rule/create` — Create rule (protected, admin)
- `POST /api/v1/point/rule/update` — Update rule (protected, admin)
- `POST /api/v1/point/rule/toggle` — Toggle rule (protected, admin)
- `POST /api/v1/point/rule/list` — List rules (protected, admin)

---

## Frontend API Services

### authService
- `login(username: String, password: String): LoginResponse` — User login
- `register(username: String, password: String, displayName: String): void` — User registration
- `refreshToken(refreshToken: String): TokenResponse` — Refresh access token
- `getCurrentUser(): UserDTO` — Get current user profile

### productService
- `listProducts(params: ListProductsParams): PageResult<ProductDTO>` — List products with filter
- `getProduct(id: Long): ProductDTO` — Get product detail
- `createProduct(data: CreateProductData): ProductDTO` — Create product
- `updateProduct(data: UpdateProductData): ProductDTO` — Update product
- `deleteProduct(id: Long): void` — Delete product
- `toggleStatus(id: Long): void` — Toggle product status

### categoryService
- `listCategories(): List<CategoryDTO>` — List all categories
- `createCategory(data: CreateCategoryData): CategoryDTO` — Create category
- `updateCategory(data: UpdateCategoryData): CategoryDTO` — Update category
- `deleteCategory(id: Long): void` — Delete category

### orderService
- `createOrder(data: CreateOrderData): OrderDTO` — Create order
- `getMyOrders(params: ListOrdersParams): PageResult<OrderDTO>` — List my orders
- `getOrder(id: Long): OrderDTO` — Get order detail
- `listAllOrders(params: ListAllOrdersParams): PageResult<OrderDTO>` — List all orders (admin)
- `confirmOrder(id: Long): OrderDTO` — Confirm order (admin)
- `rejectOrder(id: Long): OrderDTO` — Reject order (admin)

### pointsService
- `getBalance(): BalanceDTO` — Get my points balance
- `getMyHistory(params: HistoryParams): PageResult<TransactionDTO>` — Get my history
- `grant(data: GrantData): void` — Grant points (admin)
- `batchGrant(data: BatchGrantData): void` — Batch grant (admin)
- `getAllHistory(params: HistoryParams): PageResult<TransactionDTO>` — All history (admin)
- `getStatistics(): StatisticsDTO` — Points statistics (admin)
- `createRule(data: CreateRuleData): PointsRuleDTO` — Create rule (admin)
- `updateRule(data: UpdateRuleData): PointsRuleDTO` — Update rule (admin)
- `toggleRule(id: Long): void` — Toggle rule (admin)
- `listRules(): List<PointsRuleDTO>` — List rules (admin)

### userService
- `listUsers(params: ListUsersParams): PageResult<UserDTO>` — List users (admin)
- `getUser(id: Long): UserDTO` — Get user detail (admin)
- `updateUser(data: UpdateUserData): UserDTO` — Update user (admin)

### dashboardService
- `getMetrics(): DashboardMetricsDTO` — Get dashboard metrics (admin)
- `getRecentOrders(): List<OrderDTO>` — Get recent orders (admin)
