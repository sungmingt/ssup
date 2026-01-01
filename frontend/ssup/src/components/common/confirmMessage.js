export const CONFIRM_MESSAGE = {
  //delete
  DELETE_POST: (onConfirm) => ({
    title: "게시글 삭제",
    message: "정말 삭제하시겠습니까?",
    confirmText: "삭제",
    cancelText: "취소",
    onConfirm,
  }),

  DELETE_COMMENT: (onConfirm) => ({
    title: "댓글 삭제",
    message: "정말 삭제하시겠습니까?",
    confirmText: "삭제",
    cancelText: "취소",
    onConfirm,
  }),

  DELETE_USER: (onConfirm) => ({
    title: "❗️ 계정 삭제 ❗️",
    message: "정말 삭제하시겠습니까? 삭제 이후에는 복구할 수 업습니다.",
    confirmText: "삭제",
    cancelText: "취소",
    onConfirm,
  }),

  //logout
  LOGOUT: (onConfirm) => ({
    title: "로그아웃",
    message: "정말 로그아웃 하시겠습니까?",
    confirmText: "로그아웃",
    cancelText: "취소",
    onConfirm,
  }),
};
