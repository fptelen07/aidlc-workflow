import request from './request';

export const categoryService = {
  listCategories() {
    return request.post('/v1/public/category/list', {});
  },
  createCategory(data: { name: string; sort?: number }) {
    return request.post('/v1/category/create', data);
  },
  updateCategory(data: { id: number; name: string; sort?: number }) {
    return request.post('/v1/category/update', data);
  },
  deleteCategory(id: number) {
    return request.post('/v1/category/delete', { id });
  },
};
