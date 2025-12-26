import { api } from "./axios";

export const locationApi = {
  getSiDoList: () => api.get("/locations"),
  getSiGunGuList: (parentId) => api.get("/locations", { params: { parentId } }),
};
