import { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { authApi } from "@/api";
import { useNavigate } from "react-router-dom";
import "./../../css/auth/SignUp.css";
import FormLayout from "./../../layouts/FormLayout";
import { useAuthStore } from "@/store/authStore";

function Signup() {
  const navigate = useNavigate();
  const [fieldErrors, setFieldErrors] = useState({});

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

      const { code, errors } = e.response?.data || {};
      if (code === "INVALID_REQUEST") {
        // { nickname: "닉네임은 필수입니다", age: "10세 이상만..." } 형태로 변환
        const errorObj = {};
        errors.forEach((err) => {
          errorObj[err.field] = err.reason;
        });
        setFieldErrors(errorObj); // 상태 업데이트 -> UI에 빨간 글씨 노출
      }
    }
  };

  return (
    <FormLayout>
      <div className="common-card">
        <h3 className="signup-title fw-bold mb-4 text-center">회원가입</h3>

        <form onSubmit={onSubmit} noValidate>
          <div className="mb-3">
            <label className="form-label">닉네임</label>
            <input
              name="nickname"
              className="form-control"
              placeholder="닉네임"
              onChange={onChange}
              required
            />
            {/* ✅ 에러 메시지 표시 */}
            {fieldErrors.nickname && (
              <div className="invalid-feedback" style={{ display: "block" }}>
                {fieldErrors.nickname}
              </div>
            )}
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
            {/* ✅ 에러 메시지 표시 */}
            {fieldErrors.email && (
              <div className="invalid-feedback" style={{ display: "block" }}>
                {fieldErrors.email}
              </div>
            )}
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
            {/* ✅ 에러 메시지 표시 */}
            {fieldErrors.password && (
              <div className="invalid-feedback" style={{ display: "block" }}>
                {fieldErrors.password}
              </div>
            )}
          </div>

          <button className="login-btn btn btn-primary w-100">가입하기</button>
        </form>
      </div>
    </FormLayout>
  );
}

export default Signup;
