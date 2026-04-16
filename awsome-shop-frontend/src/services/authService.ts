import request from './request';

export const authService = {
  login(username: string, password: string) {
    return request.post('/v1/public/auth/login', { username, password });
  },
  register(username: string, password: string, displayName: string) {
    return request.post('/v1/public/auth/register', { username, password, displayName });
  },
  refreshToken(refreshToken: string) {
    return request.post('/v1/public/auth/refresh', { refreshToken });
  },
  getCurrentUser() {
    return request.post('/v1/auth/me', {});
  },
};
