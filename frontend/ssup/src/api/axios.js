import axios from "axios";
import { useAuthStore } from "@/store/authStore";
import { ENV } from "@/config/env";
import { useErrorStore } from "@/store/errorStore";
import { ERROR_MESSAGE } from "@/components/common/ErrorMessage";
import { useNavigate } from "react-router-dom";

export const api = axios.create({
  baseURL: ENV.API_BASE_URL + "/api",
  withCredentials: true, //쿠키를 요청에 포함
  timeout: 5000,
});

//reissue 전용 인스턴스 (인터셉터 미적용)
const reissueApi = axios.create({
  baseURL: ENV.API_BASE_URL + "/api",
  withCredentials: true,
  timeout: 5000,
});

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error = null) => {
  console.log("### PROCESS QUEUE. error=", !!error);
  failedQueue.forEach((p) => (error ? p.reject(error) : p.resolve()));
  failedQueue = []; //refresh가 끝날 때까지 기다려야 하는 “실패한 요청들”의 대기열
};

/* Access Token 만료 → API가 401 반환
이때 Refresh Token(쿠키)으로 /auth/reissue 를 호출해 Access Token을 재발급
재발급이 성공하면 방금 실패했던 API를 자동으로 다시 요청
동시에 여러 API가 401을 때려도 refresh는 딱 1번만 실행하고, 나머지는 대기(큐)
refresh가 실패하면 세션을 비우고 로그인으로 이동 */

/* use(successHandler, errorHandler) 구조
정상 응답이면 그대로 res 반환, 에러면 errorHandler로 들어옴 */

//api를 사용하는 요청에 모두 인터셉터 적용한다는 뜻
api.interceptors.response.use(
  (res) => {
    console.log("### RES OK:", res.config.url, res.status);
    return res;
  },
  async (error) => {
    const originalRequest = error.config; //error.config = “이 에러를 일으킨 원래 요청 정보” (URL, method, headers 등)

    if (originalRequest?.url?.includes("/auth/me")) {
      return Promise.resolve(error.response);
    }

    //같은 요청을 한번만 재시도하도록 플래그 설정
    //401 응답이고 첫 시도라면
    if (
      error.response?.status === 401 &&
      !originalRequest._retry &&
      !originalRequest.url.includes("/auth/me")
    ) {
      console.log("### REISSUE TRIGGERED BY:", originalRequest.url);

      //refresh가 이미 진행 중이면 큐에 넣고 기다림
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then(() => api(originalRequest));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        console.log("### REISSUE TRY");
        await reissueApi.post("/auth/reissue");
        console.log("### REISSUE SUCCESS");

        //////////
        await useAuthStore.getState().initAuth();

        processQueue(); //대기 중인 요청들 진행시키기
        return api(originalRequest); //원래 요청 재시도
      } catch (e) {
        console.log("### REISSUE FAIL");

        useAuthStore.getState().clearAuth();
        processQueue(e);

        //auth/me 요청이었다면 그냥 종료하고, 그 외의 요청이면 기존 요청을 다시 시도 (재발급에 실패했더라고 기존 요청이 허용 경로일수도 있기때문에 다시 요청해봐야함.)
        if (originalRequest.url.includes("/auth/me")) {
          return Promise.reject(e);
        }

        console.log("### RETRYING AS GUEST:", originalRequest.url);
        return api(originalRequest);
        // return Promise.resolve(error);
      } finally {
        isRefreshing = false;
      }
    }

    // --- 공통 에러 처리 ---
    const status = error.response?.status;
    const code = error.response?.data?.code;
    const errorStore = useErrorStore.getState();

    //정의 에러가 있으면, 커스텀 에러 반환
    if (code && ERROR_MESSAGE[code]) {
      errorStore.open(ERROR_MESSAGE[code]);
    } else {
      switch (status) {
        case 403:
          errorStore.open({
            title: "권한이 없습니다",
            message: "해당 기능을 사용할 수 없습니다.",
            redirect: "/",
          });
          break;

        case 404:
          errorStore.open({
            title: "페이지를 찾을 수 없습니다",
            message: "존재하지 않는 페이지입니다.",
            redirect: -1,
          });
          break;

        case 500:
          errorStore.open({
            title: "서버 오류",
            message: "잠시 후 다시 시도해주세요.",
            redirect: -1,
          });
          break;
      }
    }

    //이외의 에러는 그냥 에러 전파
    return Promise.reject(error);
  }
);
