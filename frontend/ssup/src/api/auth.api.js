import { api } from "./axios";

export const authApi = {
  signUp: (data) => api.post("/auth/signup", data),
};
