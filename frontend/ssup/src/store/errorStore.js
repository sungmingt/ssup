import { create } from "zustand";

export const useErrorStore = create((set) => ({
  error: null,

  open: (payload) => set({ error: payload }),
  close: () => set({ error: null }),
}));
