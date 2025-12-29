import { useAuthStore } from "@/store/authStore";
import { useErrorStore } from "@/store/errorStore";
import { ERROR_MESSAGE } from "./ErrorMessage";

//미로그인, PENDING 상태의 유저들의 비허용 요청을 UX 단에서 막는다. (UI 진입 가드)
//ErrorMessage를 payload로써 재사용한다.

//작업 성공시 인자(onSuccess)로 들어온 원래 하려던 동작을 수행.
//실패 시 useErrorStore.open()이 수행, GlobalErrorLayer가 재렌더됨
export const guardAuthAction = (onSuccess) => {
  const { isAuthenticated, user } = useAuthStore.getState();
  const { open } = useErrorStore.getState();

  if (!isAuthenticated) {
    open(ERROR_MESSAGE.LOGIN_REQUIRED);
    return;
  }

  if (user?.status === "PENDING") {
    open(ERROR_MESSAGE.USER_STATUS_PENDING);
    return;
  }

  onSuccess();
};
