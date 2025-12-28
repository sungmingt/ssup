import { useState } from "react";
import React from "react";

const LoginForm = ({ onLogin, onSocialLogin }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    onLogin({ email, password });
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "400px" }}>
      <h1 className="text-center mb-4">SSup!</h1>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <input
            type="email"
            className="form-control"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <input
            type="password"
            className="form-control"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary w-100 mb-3">
          Login
        </button>
        <button
          type="button"
          className="btn btn-outline-danger w-100 mb-2"
          onClick={() => onSocialLogin("GOOGLE")}
        >
          Login with Google
        </button>
        <button
          type="button"
          className="btn btn-outline-warning w-100"
          onClick={() => onSocialLogin("KAKAO")}
        >
          Login with Kakao
        </button>
      </form>
    </div>
  );
};

export default LoginForm;
