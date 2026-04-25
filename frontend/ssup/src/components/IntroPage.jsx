import "./../css/IntroPage.css";
import { useNavigate } from "react-router-dom";

const IntroPage = () => {
  const navigate = useNavigate();

  return (
    <div className="main-container">
      {/* 타이틀 */}
      <h2 className="main-title">서비스 소개</h2>

      {/* 소개 */}
      <div className="intro-card">
        <p className="intro-text">
          <span className="app-name">ssup!</span> 은 한국에 거주하는 외국인과
          외국어에 관심있는 한국인을 연결해주는
          <span className="highlight"> 언어 교류 소셜 플랫폼</span>입니다.
        </p>

        <p className="intro-sub">
          💬 온라인/오프라인에서 자유롭게 대화하며 자연스럽게 언어를 배우고
          친구를 만들 수 있어요.
        </p>
      </div>

      {/* 특징 카드 */}
      <div className="feature-grid">
        <div className="feature-card">
          <h4>👬 친구 찾기</h4>
          <p>근처에 있는 언어 교류 메이트를 찾아보세요!</p>
          <p className="feature-desc">
            🔍 언어, 지역, 관심사 필터로 원하는 친구를 쉽게 찾을 수 있어요.
          </p>
        </div>

        <div className="feature-card">
          <h4>🤖 AI 추천</h4>
          <p>내 프로필을 기반으로 최적의 친구를 추천받아보세요.</p>
          <p className="feature-desc">
            비슷한 관심사와 언어를 가진 사람을 자동으로 추천해줍니다.
          </p>
        </div>

        <div className="feature-card">
          <h4>🔐 안전한 매칭</h4>
          <p>매치가 되면 연락처가 공개됩니다.</p>
          <p className="feature-desc">
            신뢰 기반 설계로 안전하게 친구를 만날 수 있어요.
          </p>
        </div>
      </div>

      {/* CTA */}
      <div className="cta-section">
        <h4>💫 마음에 드는 친구를 찾았다면?</h4>
        <p>지금 바로 매치 요청을 보내보세요!</p>

        <div className="btn-group-row">
          <button className="community-btn" onClick={() => navigate("/posts")}>
            ✈️ 커뮤니티 바로가기
          </button>
          <button className="login-btn" onClick={() => navigate("/login")}>
            🔐 로그인 하러가기
          </button>
        </div>
      </div>
    </div>
  );
};

export default IntroPage;
