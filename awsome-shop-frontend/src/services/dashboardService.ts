import request from './request';

export const dashboardService = {
  getMetrics() {
    return request.post('/v1/dashboard/metrics', {});
  },
  getRecentOrders() {
    return request.post('/v1/dashboard/recent-orders', {});
  },
};
