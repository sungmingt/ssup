import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import "./../css/Login.css";
import kakaoIcon from "../assets/kakaoLogo.png";
import googleIcon from "../assets/googleIcon.png";
import FormLayout from "@/layouts/FormLayout";
import { useAuthStore } from "@/store/authStore";
import { authApi } from "@/api";

function Login() {
  const navigate = useNavigate();
  const API_BASE = import.meta.env.VITE_API_BASE_URL;

  const [email, setEmail] = useState("");
  const [password, setpassword] = useState("");

  const handleGoogleLogin = () => {
    window.location.href = `${API_BASE}/oauth2/authorization/google`;
  };

  const handleKakaoLogin = () => {
    window.location.href = `${API_BASE}/oauth2/authorization/kakao`;
  };

  const handleEmail = (e) => {
    setEmail(e.target.value);
  };
  const handlePassword = (e) => {
    setpassword(e.target.value);
  };
  const onSubmit = async (e) => {
    e.preventDefault();

    try {
      await authApi.login({ email, password });
      await useAuthStore.getState().userInit();

      const user = useAuthStore.getState().user;

      if (user.status === "INCOMPLETE") {
        navigate("/signup/additional");
      } else {
        navigate("/");
      }
    } catch (e) {
      alert("로그인 실패");
    }
  };

  return (
    <FormLayout>
      {/* Login Section */}
      <div className="login-wrapper d-flex justify-content-center">
        <div className="login-box p-4 shadow rounded">
          <h3 className="fw-bold mb-4 text-center">로그인</h3>

          <form onSubmit={onSubmit}>
            {/* Email */}
            <div className="mb-3">
              <label className="form-label">이메일</label>
              <input
                type="email"
                className="form-control"
                placeholder="example@email.com"
                onChange={handleEmail}
              />
            </div>

            {/* Password */}
            <div className="mb-4">
              <label className="form-label">비밀번호</label>
              <input
                type="password"
                className="form-control"
                placeholder="비밀번호"
                onChange={handlePassword}
              />
            </div>

            <button
              className="login-btn btn btn-primary mb-3 w-100"
              type="submit"
            >
              로그인
            </button>
          </form>

          <div className="divider text-center my-3">또는</div>

          {/* 이메일로 가입하기 */}
          <button
            className="btn email-btn btn-outline-dark w-100 mb-2"
            onClick={() => navigate("/signup")}
          >
            💌 이메일로 계속하기
          </button>

          {/* 소셜 로그인 */}
          <button
            className="btn btn-light border w-100 mb-2 d-flex align-items-center justify-content-center gap-2"
            onClick={handleGoogleLogin}
          >
            <img src={googleIcon} width="20" height="20" alt="google" />
            <span>Google로 계속하기</span>
          </button>

          <button
            className="btn btn-warning w-100 d-flex align-items-center justify-content-center gap-2"
            onClick={handleKakaoLogin}
          >
            <img src={kakaoIcon} width="22" height="22" alt="kakao" />
            <span>Kakao로 계속하기</span>
          </button>
        </div>
      </div>
    </FormLayout>
  );
}

export default Login;
