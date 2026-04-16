# Unit 6 — Frontend Code Summary

## Overview
Implemented all frontend pages, API services, and integrations for the AWSome Shop points redemption platform.

## Changes

### 1. API Services (7 new files in `src/services/`)
- `authService.ts` — login, register, refreshToken, getCurrentUser
- `productService.ts` — listProducts, getProduct, createProduct, updateProduct, deleteProduct, toggleStatus
- `categoryService.ts` — listCategories, createCategory, updateCategory, deleteCategory
- `orderService.ts` — createOrder, getMyOrders, getOrder, listAllOrders, confirmOrder, rejectOrder
- `pointsService.ts` — getBalance, getMyHistory, grant, batchGrant, getAllHistory, getStatistics, createRule, updateRule, toggleRule, listRules
- `userService.ts` — listUsers, getUser, updateUser
- `dashboardService.ts` — getMetrics, getRecentOrders

All services use POST method via the shared axios instance in `request.ts`.

### 2. Modified Files
- `src/services/request.ts` — Token refresh logic with 401 retry queue; uses `accessToken`/`refreshToken` from localStorage
- `src/store/useAuthStore.ts` — Replaced mock login with real `authService.login` + `getCurrentUser`; stores tokens in localStorage
- `src/pages/Login/index.tsx` — No code changes needed (already calls store.login)
- `src/pages/ShopHome/index.tsx` — Replaced mock data with real `productService.listProducts` + `categoryService.listCategories`
- `src/pages/Dashboard/index.tsx` — Replaced mock data with `dashboardService.getMetrics` + `getRecentOrders`
- `src/router/index.tsx` — Added all 9 new routes
- `src/i18n/locales/zh.json` — Added all new page translation keys
- `src/i18n/locales/en.json` — Added all new page translation keys

### 3. New Pages (9 pages)
| Page | Path | User Story |
|------|------|------------|
| Register | `/register` | US-008 |
| ProductDetail | `/product/:id` | US-014 |
| MyOrders | `/orders` | US-017 |
| MyPoints | `/points` | US-021 |
| AdminProducts | `/admin/products` | US-012 |
| AdminCategories | `/admin/categories` | US-013 |
| AdminOrders | `/admin/orders` | US-016 |
| AdminPoints | `/admin/points` | US-020 |
| AdminUsers | `/admin/users` | US-022 |

### 4. Navigation
- EmployeeLayout sidebar already had nav items for Home, Orders, Points
- AdminLayout sidebar already had nav items for Dashboard, Products, Categories, Points, Orders, Users

### 5. Build Verification
- `npx tsc -b` passes with zero errors on EC2
