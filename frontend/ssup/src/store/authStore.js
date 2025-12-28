import { create } from "zustand";
import { authApi } from "@/api";

//유저를 기억하는 곳
//브라우저 메모리 상의 인증 세션
export const useAuthStore = create((set) => ({
  user: null,
  loading: true,

  initAuth: async () => {
    try {
      const res = await authApi.me();
      set({ user: res.data, loading: false });
    } catch {
      set({ user: null, loading: false });
    }
  },

  logout: () => set({ user: null }),

  clearAuth: () => set({ user: null, loading: false }),
}));
