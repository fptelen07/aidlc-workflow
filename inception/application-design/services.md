# Service Definitions & Orchestration Patterns

## Services

### 1. Auth Service

Orchestrates user registration and authentication.

- **Login flow**: validate credentials → generate JWT tokens → return user info
- **Register flow**: validate uniqueness → hash password → create user
- **Token validation**: verify JWT signature → extract claims → return operatorId

### 2. Product Service

Manages product catalog.

- Product CRUD with status management (on/off shelf)
- Category CRUD with referential integrity (cannot delete category with products)
- Stock deduction called by Order Service during order confirmation

### 3. Order Service

Orchestrates redemption flow.

- **Create order**: validate product (call Product Service) → validate points (call Points Service) → create pending order
- **Confirm order**: update status → deduct points (call Points Service) → deduct stock (call Product Service)
- **Reject order**: update status only

### 4. Points Service

Manages points lifecycle.

- Grant/deduct with transaction logging
- Batch grant for multiple users
- Expiration processing (scheduled)
- Statistics aggregation
- Rule configuration for automated grants

### 5. Gateway Service

Routes requests, authenticates via Auth Service, injects operatorId.

### 6. Frontend

SPA consuming all backend APIs via Gateway.

## Cross-Service Communication

Synchronous HTTP via Gateway. Order Service calls Product Service and Points Service through Gateway routes. Each call is a POST request with the same gateway-injectable pattern.

## Orchestration Patterns

Order confirmation is a multi-step orchestration:

```
OrderAppService.confirmOrder()
  → calls PointsService.deduct() via HTTP
  → calls ProductService.deductStock() via HTTP
  → updates order status
```

**Failure handling**:
- If points deduction fails → order stays pending
- If stock deduction fails after points deducted → compensation (refund points)
