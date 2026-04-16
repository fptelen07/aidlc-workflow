import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { authService } from '../services/authService';

export type UserRole = 'employee' | 'admin';

export interface UserInfo {
  id?: number;
  username: string;
  displayName: string;
  role: UserRole;
  points?: number;
  avatar?: string;
}

interface AuthState {
  user: UserInfo | null;
  isAuthenticated: boolean;
  accessToken: string | null;
  refreshToken: string | null;
  login: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
  fetchCurrentUser: () => Promise<void>;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      isAuthenticated: false,
      accessToken: null,
      refreshToken: null,
      login: async (username: string, password: string) => {
        try {
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          const res: any = await authService.login(username, password);
          const data = res?.data ?? res;
          const accessToken = data.accessToken || data.token;
          const refreshToken = data.refreshToken;
          if (accessToken) {
            localStorage.setItem('accessToken', accessToken);
            if (refreshToken) localStorage.setItem('refreshToken', refreshToken);
            set({ accessToken, refreshToken });

            // Fetch current user info
            try {
              // eslint-disable-next-line @typescript-eslint/no-explicit-any
              const userRes: any = await authService.getCurrentUser();
              const userData = userRes?.data ?? userRes;
              const userInfo: UserInfo = {
                id: userData.id,
                username: userData.username,
                displayName: userData.displayName || userData.username,
                role: userData.role === 'admin' ? 'admin' : 'employee',
                points: userData.points ?? 0,
                avatar: userData.avatar,
              };
              set({ user: userInfo, isAuthenticated: true });
            } catch {
              // If getCurrentUser fails, extract user from login response
              const loginUser = data.user || data;
              const userInfo: UserInfo = {
                id: loginUser.id || data.userId,
                username: loginUser.username || username,
                displayName: loginUser.displayName || username,
                role: loginUser.role === 'admin' ? 'admin' : 'employee',
                points: loginUser.points ?? 0,
                avatar: loginUser.avatar,
              };
              set({ user: userInfo, isAuthenticated: true });
            }
            return true;
          }
          return false;
        } catch {
          return false;
        }
      },
      logout: () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        set({ user: null, isAuthenticated: false, accessToken: null, refreshToken: null });
      },
      fetchCurrentUser: async () => {
        try {
          // eslint-disable-next-line @typescript-eslint/no-explicit-any
          const res: any = await authService.getCurrentUser();
          const data = res?.data ?? res;
          const userInfo: UserInfo = {
            id: data.id,
            username: data.username,
            displayName: data.displayName || data.username,
            role: data.role === 'admin' ? 'admin' : 'employee',
            points: data.points ?? 0,
            avatar: data.avatar,
          };
          set({ user: userInfo });
        } catch { /* ignore */ }
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
        accessToken: state.accessToken,
        refreshToken: state.refreshToken,
      }),
    },
  ),
);
