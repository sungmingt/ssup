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
    message: "다른 이메일을 사용해주세요.",
  },

  EMAIL_NOT_EXISTS: {
    title: "존재하지 않는 이메일입니다",
    message: "이메일을 다시 확인해주세요.",
  },

  PASSWORD_NOT_MATCH: {
    title: "비밀번호가 일치하지 않습니다.",
    message: "비밀번호를 다시 확인해주세요.",
  },

  NICKNAME_ALREADY_EXISTS: {
    title: "이미 사용중인 닉네임입니다",
    message: "다른 닉네임을 사용해주세요.",
  },

  USER_NOT_FOUND: {
    title: "존재하지 않는 회원입니다.",
    message: "탈퇴했거나 존재하지 않는 회원입니다.",
  },

  LOGIN_REQUIRED: {
    title: "로그인이 필요한 기능입니다",
    message: "로그인 후 이용해주세요.",
  },

  USER_STATUS_PENDING: {
    title: "프로필 미완성 상태입니다",
    message: "프로필 정보 입력을 완료해주세요.",
    redirect: "/signup/additional",
  },

  DELETED_USER: {
    title: "탈퇴한 계정입니다",
    message:
      "탈퇴한 계정의 이메일 또는 정보가 포함되어있습니다. 다른 계정을 이용해주세요",
  },

  //image
  FILE_SIZE_EXCEEDED: {
    title: "이미지 크기 제한 초과",
    message: "5MB 이하의 이미지만 업로드할 수 있습니다.",
  },

  //global
  INTERNAL_SERVER_ERROR: {
    title: "서버에서 오류가 발생했습니다.",
    message: "홈 화면으로 이동합니다.",
    redirect: "/",
  },
};
