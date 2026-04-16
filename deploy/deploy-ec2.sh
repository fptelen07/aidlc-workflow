#!/bin/bash
# EC2 deployment script - called by GitHub Actions
set -e

echo "=== Stopping old services ==="
pkill -f 'auth-service.jar\|product-service.jar\|points-service.jar\|order-service.jar\|gateway-service.jar' 2>/dev/null || true
sleep 3

echo "=== Deploying JARs ==="
mkdir -p ~/awsome-shop-jars
cp *.jar ~/awsome-shop-jars/

echo "=== Deploying frontend ==="
mkdir -p ~/awsome-shop/awsome-shop-frontend
rm -rf ~/awsome-shop/awsome-shop-frontend/dist
cp -r frontend-dist ~/awsome-shop/awsome-shop-frontend/dist

echo "=== Starting services ==="
cd ~/awsome-shop-jars
nohup java -jar auth-service.jar --spring.profiles.active=local > /tmp/auth.log 2>&1 &
nohup java -jar product-service.jar --spring.profiles.active=local > /tmp/product.log 2>&1 &
nohup java -jar points-service.jar --spring.profiles.active=local > /tmp/points.log 2>&1 &
nohup java -jar order-service.jar --spring.profiles.active=local > /tmp/order.log 2>&1 &
nohup java -jar gateway-service.jar --spring.profiles.active=local > /tmp/gateway.log 2>&1 &

echo "=== Waiting for services to start ==="
sleep 30

echo "=== Health check ==="
for p in 8001 8002 8003 8004 8080; do
  ss -tlnp 2>/dev/null | grep :$p > /dev/null && echo "Port $p: UP ✅" || echo "Port $p: DOWN ❌"
done

echo "=== Deploy complete ==="
