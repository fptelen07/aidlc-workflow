import request from './request';

export const pointsService = {
  getBalance() {
    return request.post('/v1/point/balance', {});
  },
  getMyHistory(params: { page?: number; size?: number; type?: string }) {
    return request.post('/v1/point/my-history', params);
  },
  grant(data: { userId: number; amount: number; reason: string }) {
    return request.post('/v1/point/grant', data);
  },
  batchGrant(data: { userIds: number[]; amount: number; reason: string }) {
    return request.post('/v1/point/batch-grant', data);
  },
  getAllHistory(params: { page?: number; size?: number; type?: string }) {
    return request.post('/v1/point/history', params);
  },
  getStatistics() {
    return request.post('/v1/point/statistics', {});
  },
  createRule(data: Record<string, unknown>) {
    return request.post('/v1/point/rule/create', data);
  },
  updateRule(data: Record<string, unknown>) {
    return request.post('/v1/point/rule/update', data);
  },
  toggleRule(id: number) {
    return request.post('/v1/point/rule/toggle', { id });
  },
  listRules() {
    return request.post('/v1/point/rule/list', {});
  },
};
