import "./../css/Header.css";

import { Link } from "react-router-dom";
import { useState } from "react";

function Header() {
  // 예: 로그인 상태 (실무에서는 context나 recoil, redux, query로 관리)
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // 예: 로그인된 유저 정보
  const user = {
    name: "성민",
    avatar: "https://via.placeholder.com/32?text=U",
  };

  return (
    <header className="sticky-top bg-white shadow-sm">
      <nav
        className="navbar navbar-expand-lg navbar-light bg-white py-3"
        aria-label="Main navigation"
      >
        <div className="container px-4">
          {/* 브랜드 로고 */}
          <Link className="navbar-brand fw-bold fs-3" to="/">
            Suup!
          </Link>

          {/* 모바일 토글 버튼 */}
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#mainNavbar"
            aria-controls="mainNavbar"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          {/* 메뉴 */}
          <div
            className="collapse navbar-collapse justify-content-end"
            id="mainNavbar"
          >
            <ul className="navbar-nav align-items-center gap-3">
              <li className="nav-item">
                <Link className="nav-link fw-semibold" to="/posts">
                  커뮤니티
                </Link>
              </li>

              <li className="nav-item">
                <Link className="nav-link fw-semibold" to="/friends">
                  친구 찾기
                </Link>
              </li>

              {/* 로그인 여부에 따른 UI 분기 */}
              {!isLoggedIn ? (
                <li className="nav-item">
                  <Link className="btn btn-outline-primary px-3" to="/login">
                    로그인
                  </Link>
                </li>
              ) : (
                <li className="nav-item dropdown">
                  <button
                    className="btn dropdown-toggle d-flex align-items-center gap-2"
                    id="userMenuDropdown"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                    style={{ background: "transparent" }}
                  >
                    <img
                      src={user.avatar}
                      alt="avatar"
                      className="rounded-circle"
                      width="32"
                      height="32"
                    />
                    <span className="fw-semibold">{user.name}</span>
                  </button>

                  <ul className="dropdown-menu dropdown-menu-end">
                    <li>
                      <Link className="dropdown-item" to="/profile">
                        내 프로필
                      </Link>
                    </li>
                    <li>
                      <Link className="dropdown-item" to="/settings">
                        설정
                      </Link>
                    </li>
                    <li>
                      <button
                        className="dropdown-item text-danger"
                        onClick={() => setIsLoggedIn(false)}
                      >
                        로그아웃
                      </button>
                    </li>
                  </ul>
                </li>
              )}
            </ul>
          </div>
        </div>
      </nav>
    </header>
  );
}

export default Header;
