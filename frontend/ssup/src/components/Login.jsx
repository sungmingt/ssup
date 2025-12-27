import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import "./../css/Login.css";
import kakaoIcon from "../assets/kakaoIcon.png";
import googleIcon from "../assets/googleIcon.png";

function Login() {
  const navigate = useNavigate();

  return (
    <>
      {/* Login Section */}
      <div className="login-wrapper d-flex justify-content-center">
        <div className="login-box p-4 shadow rounded">
          <h3 className="fw-bold mb-4 text-center">로그인</h3>

          {/* Email */}
          <div className="mb-3">
            <label className="form-label">이메일</label>
            <input
              type="email"
              className="form-control"
              placeholder="example@email.com"
            />
          </div>

          {/* Password */}
          <div className="mb-4">
            <label className="form-label">비밀번호</label>
            <input
              type="password"
              className="form-control"
              placeholder="비밀번호"
            />
          </div>

          <button className="login-btn btn btn-primary mb-3 w-100">
            로그인
          </button>

          <div className="divider text-center my-3">또는</div>

          <button
            className="btn btn-outline-dark w-100 mb-3"
            onClick={() => navigate("/signup")}
          >
            ✉️ 이메일로 가입하기
          </button>

          {/* Social Buttons */}
          <button className="btn btn-light border w-100 mb-2 d-flex align-items-center justify-content-center gap-2">
            <img src={googleIcon} width="20" height="20" alt="google" />
            <span>Google로 계속하기</span>
          </button>

          <button className="btn btn-warning w-100 d-flex align-items-center justify-content-center gap-2">
            <img src={kakaoIcon} width="22" height="22" alt="kakao" />
            <span>Kakao로 계속하기</span>
          </button>
        </div>
      </div>
    </>
  );
}

export default Login;
