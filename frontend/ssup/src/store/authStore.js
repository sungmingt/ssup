import { create } from "zustand";
import { authApi } from "@/api";

//유저를 기억하는 곳
//브라우저 메모리 상의 인증 세션
export const useAuthStore = create((set) => ({
  user: null,
  isAuthenticated: false,
  loading: true,

  initAuth: async () => {
    try {
      set({ loading: true });
      const res = await authApi.me();

      if (res.data) {
        set({ user: res.data, isAuthenticated: true });
      } else {
        set({ user: null, isAuthenticated: false });
      }
    } catch {
      set({ user: null, isAuthenticated: false });
    } finally {
      set({ loading: false });
    }
  },

  //유저 정보 최신 업데이트. (로그인 직후, 프로필 수정 후 등)
  userInit: async () => {
    try {
      const res = await authApi.me();
      if (res.data) set({ user: res.data, isAuthenticated: true });
      else set({ user: null, isAuthenticated: false });
    } catch {
      set({ user: null, isAuthenticated: false });
    }
  },

  clearAuth: () => {
    set({ user: null, isAuthenticated: false });
  },

  logout: async () => {
    try {
      await authApi.logout();
    } finally {
      set({ user: null, isAuthenticated: false });
    }
  },
}));
