#!/bin/bash
cd ~/awsome-shop

# Kill old processes
ps aux | grep 'awsome-shop.*SNAPSHOT.jar' | grep -v grep | awk '{print $2}' | xargs -r kill -9 2>/dev/null
sleep 2

# Start services with correct JAR names
nohup java -jar awsome-shop-auth-service/bootstrap/target/awsome-shop-auth-service-1.0.0-SNAPSHOT.jar --spring.profiles.active=local > /tmp/auth.log 2>&1 &
nohup java -jar awsome-shop-product-service/bootstrap/target/awsome-shop-product-service-1.0.0-SNAPSHOT.jar --spring.profiles.active=local > /tmp/product.log 2>&1 &
nohup java -jar awsome-shop-points-service/bootstrap/target/awsome-shop-point-service-1.0.0-SNAPSHOT.jar --spring.profiles.active=local > /tmp/points.log 2>&1 &
nohup java -jar awsome-shop-order-service/bootstrap/target/awsome-shop-order-service-1.0.0-SNAPSHOT.jar --spring.profiles.active=local > /tmp/order.log 2>&1 &
nohup java -jar awsome-shop-gateway-service/bootstrap/target/awsome-shop-gateway-service-1.0.0-SNAPSHOT.jar --spring.profiles.active=local > /tmp/gateway.log 2>&1 &

echo "All 5 services starting in background"
