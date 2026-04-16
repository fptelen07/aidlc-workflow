import request from './request';

export const userService = {
  listUsers(params: { page?: number; size?: number; keyword?: string }) {
    return request.post('/v1/auth/user/list', params);
  },
  getUser(id: number) {
    return request.post('/v1/user/get', { id });
  },
  updateUser(data: Record<string, unknown>) {
    return request.post('/v1/user/update', data);
  },
};
