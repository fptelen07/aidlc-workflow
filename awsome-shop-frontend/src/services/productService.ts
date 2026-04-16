import request from './request';

export interface ListProductsParams {
  page?: number;
  size?: number;
  name?: string;
  category?: string;
}

export const productService = {
  listProducts(params: ListProductsParams) {
    return request.post('/v1/public/product/list', params);
  },
  getProduct(id: number) {
    return request.post('/v1/public/product/get', { id });
  },
  createProduct(data: Record<string, unknown>) {
    return request.post('/v1/product/create', data);
  },
  updateProduct(data: Record<string, unknown>) {
    return request.post('/v1/product/update', data);
  },
  deleteProduct(id: number) {
    return request.post('/v1/product/delete', { id });
  },
  toggleStatus(id: number) {
    return request.post('/v1/product/toggle-status', { id });
  },
};
