# Build Instructions — AWSome Shop

## 前置条件
- **Java**: 21+
- **Maven**: 3.9+
- **Node.js**: 20+
- **npm**: 10+
- **MySQL**: 8.0+
- **Redis**: 7+
- **Docker**: 24+ (可选，用于 docker-compose)

## 环境配置

### 数据库
每个后端服务需要独立的 MySQL 数据库：
```bash
mysql -u root -p -e "
CREATE DATABASE IF NOT EXISTS awsome_shop_auth;
CREATE DATABASE IF NOT EXISTS awsome_shop_product;
CREATE DATABASE IF NOT EXISTS awsome_shop_order;
CREATE DATABASE IF NOT EXISTS awsome_shop_points;
CREATE DATABASE IF NOT EXISTS awsome_shop_gateway;
"
```

### Redis
```bash
redis-server  # 默认 localhost:6379
```

## 构建步骤

### 1. 后端服务编译（5 个服务）
```bash
cd awsome-shop-auth-service && mvn clean compile -q && echo "✅ auth-service"
cd ../awsome-shop-product-service && mvn clean compile -q && echo "✅ product-service"
cd ../awsome-shop-order-service && mvn clean compile -q && echo "✅ order-service"
cd ../awsome-shop-points-service && mvn clean compile -q && echo "✅ points-service"
cd ../awsome-shop-gateway-service && mvn clean compile -q && echo "✅ gateway-service"
```

### 2. 后端服务打包
```bash
cd awsome-shop-auth-service && mvn clean package -DskipTests -q
cd ../awsome-shop-product-service && mvn clean package -DskipTests -q
cd ../awsome-shop-order-service && mvn clean package -DskipTests -q
cd ../awsome-shop-points-service && mvn clean package -DskipTests -q
cd ../awsome-shop-gateway-service && mvn clean package -DskipTests -q
```

### 3. 前端构建
```bash
cd awsome-shop-frontend
npm install
npm run build
```

### 4. 启动服务（按依赖顺序）
```bash
# 1. Auth Service (port 8001)
cd awsome-shop-auth-service && java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar --spring.profiles.active=local &

# 2. Product Service (port 8002)
cd awsome-shop-product-service && java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar --spring.profiles.active=local &

# 3. Points Service (port 8004)
cd awsome-shop-points-service && java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar --spring.profiles.active=local &

# 4. Order Service (port 8003)
cd awsome-shop-order-service && java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar --spring.profiles.active=local &

# 5. Gateway Service (port 8080)
cd awsome-shop-gateway-service && java -jar bootstrap/target/bootstrap-1.0.0-SNAPSHOT.jar --spring.profiles.active=local &

# 6. Frontend (port 5173)
cd awsome-shop-frontend && npm run dev &
```

## Docker Compose（可选）
如果 deploy/ 目录有 docker-compose.yml，可以使用：
```bash
cd deploy && docker-compose up -d
```

## 验证
- Auth: `curl -X POST http://localhost:8001/api/v1/public/auth/login -H 'Content-Type: application/json' -d '{"username":"admin","password":"admin123"}'`
- Gateway: `curl http://localhost:8080/actuator/health`
- Frontend: 浏览器访问 `http://localhost:5173`
