import { create } from "zustand";
import { authApi } from "@/api";

//유저를 기억하는 곳
//브라우저 메모리 상의 인증 세션
export const useAuthStore = create((set) => ({
  user: null,
  isAuthenticated: false,
  loading: true,

  //유저 로그인 상태 체크. (앱 진입, 새로고침 시 실행하도록)
  initAuth: async () => {
    try {
      const res = await authApi.me();
      set({ user: res.data, isAuthenticated: true, loading: false });
    } catch {
      set({ user: null, isAuthenticated: false, loading: false });
    }
  },

  //유저 정보 최신 업데이트. (로그인 직후, 프로필 수정 후 등)
  userInit: async () => {
    try {
      const res = await authApi.me();
      set({ user: res.data, isAuthenticated: true });
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
      window.location.href = "/";
    }
  },
}));
