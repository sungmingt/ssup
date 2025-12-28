export const ERROR_MESSAGE = {
  //post
  POST_NOT_FOUND: {
    title: "게시글을 찾을 수 없습니다",
    message: "삭제되었거나 존재하지 않는 게시글입니다.",
    redirect: -1,
  },

  NOT_POST_OWNER: {
    title: "권한이 없습니다",
    message: "작성자만 수정할 수 있습니다.",
  },

  //comment
  COMMENT_NOT_FOUND: {
    title: "댓글을 찾을 수 없습니다",
    message: "삭제되었거나 존재하지 않는 댓글입니다.",
    redirect: -1,
  },

  NOT_COMMENT_OWNER: {
    title: "권한이 없습니다",
    message: "작성자만 수정할 수 있습니다.",
  },

  //auth
  TOKEN_EXPIRED: {
    title: "세션이 만료되었습니다",
    message: "다시 로그인해주세요.",
    redirect: "/login",
  },

  REFRESH_TOKEN_EXPIRED: {
    title: "세션이 만료되었습니다",
    message: "다시 로그인해주세요.",
    redirect: "/login",
  },

  //user
  EMAIL_ALREADY_EXISTS: {
    title: "이미 사용 중인 이메일입니다",
    message: "다른 이메일을 입력해주세요.",
  },

  USER_NOT_FOUND: {
    title: "존재하지 않는 회원입니다.",
    message: "탈퇴했거나 존재하지 않는 회원입니다.",
  },

  //global
  INTERNAL_SERVER_ERROR: {
    title: "서버에서 오류가 발생했습니다.",
    message: "홈 화면으로 이동합니다.",
    redirect: "/",
  },
};
