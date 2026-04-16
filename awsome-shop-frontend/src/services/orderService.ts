import request from './request';

export const orderService = {
  createOrder(data: { productId: number; quantity?: number }) {
    return request.post('/v1/order/create', data);
  },
  getMyOrders(params: { page?: number; size?: number; status?: string }) {
    return request.post('/v1/order/my-list', params);
  },
  getOrder(id: number) {
    return request.post('/v1/order/get', { id });
  },
  listAllOrders(params: { page?: number; size?: number; status?: string; userName?: string }) {
    return request.post('/v1/order/list', params);
  },
  confirmOrder(id: number) {
    return request.post('/v1/order/confirm', { id });
  },
  rejectOrder(id: number) {
    return request.post('/v1/order/reject', { id });
  },
};
