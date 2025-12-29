import { create } from "zustand";

/* 어디서든 confirmStore.open()만 호출하면 Confirm 모달 띄움 + 확인/취소 클릭 시 후속 로직 실행 */
export const useConfirmStore = create((set) => ({
  confirm: null, //모달을 오픈할지 결정 (null이면 모달이 닫힌 상태, 객체이면 열린 상태(객체는 모달의 내용))

  open: (payload) => set({ confirm: payload }),
  close: () => set({ confirm: null }),
}));
