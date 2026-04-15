# API Documentation — Awsome Shop

## 1. API Gateway Routing

All client requests pass through the API Gateway (port 8000), which handles JWT validation and header injection before forwarding to downstream services.

### Route Table

| Route Pattern | Target Service | Port | Auth Required |
|---|---|---|---|
| `/api/v1/public/auth/**` | auth-service | 8001 | No |
| `/api/v1/internal/auth/**` | auth-service | 8001 | Yes |
| `/api/v1/public/product/**` | product-service | 8002 | No |
| `/api/v1/product/**` | product-service | 8002 | Yes |
| `/api/v1/public/point/**` | point-service | 8003 | No |
| `/api/v1/point/**` | point-service | 8003 | Yes |
| `/api/v1/public/order/**` | order-service | 8004 | No |
| `/api/v1/order/**` | order-service | 8004 | Yes |
| `/v3/api-docs/{service}` | respective service | — | No |

**Routing conventions:**
- `/api/v1/public/**` — unauthenticated access
- `/api/v1/**` (non-public) — requires valid JWT
- `/api/v1/internal/**` — service-to-service calls (gateway-initiated)

## 2. Common Request/Response Patterns

### 2.1 Request Conventions

- **All endpoints use HTTP POST method**
- Gateway injects tenant/user context into every forwarded request via `GatewayInjectableRequest`:

```json
{
  "tenantId": "string",
  "traceId": "string",
  "userId": "string"
}
```

- Paginated requests extend `PageableRequest`:

```json
{
  "tenantId": "string",
  "traceId": "string",
  "userId": "string",
  "page": 0,
  "size": 10
}
```

### 2.2 Response Wrapper

All responses use `Result<T>`:

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

- `code` — `0` = success, non-zero = error
- `message` — human-readable status
- `data` — response payload (type varies)

### 2.3 Error Responses

**Validation error** (`ErrorDetail`):

```json
{
  "field": "username",
  "message": "must not be blank"
}
```

**Gateway error** (`ErrorResponse`):

```json
{
  "code": 401,
  "message": "Unauthorized",
  "requestId": "uuid",
  "path": "/api/v1/order/create",
  "timestamp": "2026-04-15T07:00:00Z"
}
```

## 3. Implemented APIs

### 3.1 Gateway Internal API

| Endpoint | Description |
|---|---|
| `POST /api/v1/internal/auth/validate` | Validate JWT token (gateway → auth-service) |

**Request:**

```json
{
  "token": "string"
}
```

**Response:**

```json
{
  "success": true,
  "operatorId": "string",
  "message": "string"
}
```

### 3.2 Test CRUD Endpoints (Placeholder)

Present in all services as scaffolding. All use POST and follow the common request/response pattern.

| Endpoint | Description |
|---|---|
| `POST /api/v1/public/test/get` | Get entity by ID |
| `POST /api/v1/public/test/list` | List entities with pagination |
| `POST /api/v1/public/test/create` | Create entity |
| `POST /api/v1/public/test/update` | Update entity |
| `POST /api/v1/public/test/delete` | Delete entity |

## 4. Expected APIs (Not Yet Implemented)

Inferred from frontend mock data and UI structure.

### 4.1 Auth Service

| Endpoint (expected) | Description | Auth |
|---|---|---|
| `POST /api/v1/public/auth/login` | User login | No |
| `POST /api/v1/public/auth/register` | User registration | No |
| `POST /api/v1/auth/logout` | Logout | Yes |
| `POST /api/v1/auth/me` | Get current user info | Yes |

### 4.2 Product Service

| Endpoint (expected) | Description | Auth |
|---|---|---|
| `POST /api/v1/public/product/list` | List products (filter by category, search, pagination) | No |
| `POST /api/v1/public/product/detail` | Get product detail | No |
| `POST /api/v1/product/create` | Create product (admin) | Yes |
| `POST /api/v1/product/update` | Update product (admin) | Yes |
| `POST /api/v1/product/delete` | Delete product (admin) | Yes |
| `POST /api/v1/public/product/categories` | List categories | No |
| `POST /api/v1/product/category/create` | Create category (admin) | Yes |
| `POST /api/v1/product/category/update` | Update category (admin) | Yes |
| `POST /api/v1/product/category/delete` | Delete category (admin) | Yes |

### 4.3 Order Service

| Endpoint (expected) | Description | Auth |
|---|---|---|
| `POST /api/v1/order/create` | Create redemption order | Yes |
| `POST /api/v1/order/list` | List orders (own for employee, all for admin) | Yes |
| `POST /api/v1/order/detail` | Get order detail | Yes |
| `POST /api/v1/order/update-status` | Update order status (admin) | Yes |

### 4.4 Point Service

| Endpoint (expected) | Description | Auth |
|---|---|---|
| `POST /api/v1/point/balance` | Get point balance | Yes |
| `POST /api/v1/point/transactions` | Transaction history | Yes |
| `POST /api/v1/point/grant` | Grant points to user (admin) | Yes |
| `POST /api/v1/point/stats` | Point statistics (admin) | Yes |

### 4.5 User Management (via Auth Service)

| Endpoint (expected) | Description | Auth |
|---|---|---|
| `POST /api/v1/auth/users` | List users (admin) | Yes |
| `POST /api/v1/auth/user/detail` | Get user detail (admin) | Yes |
| `POST /api/v1/auth/user/update` | Update user (admin) | Yes |
| `POST /api/v1/auth/user/delete` | Delete user (admin) | Yes |

### 4.6 Dashboard (Admin)

| Endpoint (expected) | Description | Auth |
|---|---|---|
| `POST /api/v1/order/dashboard/metrics` | Dashboard summary metrics | Yes |
| `POST /api/v1/order/dashboard/recent` | Recent orders | Yes |

## 5. Data Models

Inferred from frontend mock data.

### UserInfo

| Field | Type | Description |
|---|---|---|
| username | String | Login identifier |
| displayName | String | Display name |
| role | String | `employee` or `admin` |
| points | Integer | Current point balance |
| avatar | String | Avatar URL |

### Product

| Field | Type | Description |
|---|---|---|
| id | String | Product identifier |
| name | String | Product name |
| category | String | Category key |
| categoryLabel | String | Category display name |
| rating | Decimal | Average rating |
| reviews | Integer | Review count |
| sold | Integer | Units redeemed |
| points | Integer | Point cost |
| image | String | Image URL |
| tag | String | Badge text (e.g., "Hot", "New") |
| tagColor | String | Badge color |
| bgColor | String | Card background color |

### Category

| Field | Type | Description |
|---|---|---|
| key | String | Category identifier |
| label | String | Display name |

### Order

| Field | Type | Description |
|---|---|---|
| id | String | Order identifier |
| user | String | Username of orderer |
| product | String | Product name |
| points | Integer | Points spent |
| status | String | `completed`, `pending`, or `processing` |
| time | String | Order timestamp |

### Dashboard Metrics

| Field | Type | Description |
|---|---|---|
| totalProducts | Integer | Total product count |
| totalUsers | Integer | Total user count |
| monthlyRedemptions | Integer | Redemptions this month |
| pointsCirculation | Integer | Total points in circulation |
