import axios from "axios";
import { useAuthStore } from "@/store/authStore";
import { authApi } from "@/api";
import { ENV } from "@/config/env";

export const api = axios.create({
  baseURL: ENV.API_BASE_URL + "/api",
  withCredentials: true,
  timeout: 5000,
});

//accessToken이 만료되어 401이 발생했을때만 동작
api.interceptors.response.use(
  (res) => res,
  async (err) => {
    const original = err.config;

    if (err.response?.status === 401 && !original._retry) {
      original._retry = true;
      try {
        await authApi.reissue();
        await useAuthStore.getState().initAuth();
        return api(original);
      } catch {
        useAuthStore.getState().clearAuth();
        window.location.href = "/login";
      }
    }
    return Promise.reject(err);
  }
);
