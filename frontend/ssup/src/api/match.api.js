import { api } from "./axios";

export const matchApi = {
  sendRequest: (formData) => api.get("match/request", formData),
};
