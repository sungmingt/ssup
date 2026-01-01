import { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { authApi } from "@/api";
import { useNavigate } from "react-router-dom";
import "./../../css/auth/SignUp.css";
import FormLayout from "./../../layouts/FormLayout";
import { useAuthStore } from "@/store/authStore";

function Signup() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nickname: "",
    email: "",
    password: "",
  });

  const onChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await authApi.signUp(form);

      alert("회원가입이 완료되었습니다. 로그인 해주세요.");
      navigate("/login");
    } catch (e) {
      console.error(e);
      alert("회원가입 실패");
    }
  };

  return (
    <FormLayout>
      <div className="common-card">
        <h3 className="signup-title fw-bold mb-4 text-center">회원가입</h3>

        <form onSubmit={onSubmit}>
          <div className="mb-3">
            <label className="form-label">닉네임</label>
            <input
              name="nickname"
              className="form-control"
              placeholder="닉네임"
              onChange={onChange}
              required
            />
          </div>

          <div className="mb-3">
            <label className="form-label">이메일</label>
            <input
              type="email"
              name="email"
              className="form-control"
              placeholder="example@email.com"
              onChange={onChange}
              required
            />
          </div>

          <div className="mb-4">
            <label className="form-label">비밀번호</label>
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="비밀번호"
              onChange={onChange}
              required
            />
          </div>

          <button className="login-btn btn btn-primary w-100">가입하기</button>
        </form>
      </div>
    </FormLayout>
  );
}

export default Signup;
