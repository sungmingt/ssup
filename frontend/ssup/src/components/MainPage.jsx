import mainImage1 from "./../assets/main_logo_1.png";
import mainImage2 from "./../assets/main_logo_2.png";
import mainImage3 from "./../assets/main_logo_3.png";
import "./../css/MainPage.css";
import { useNavigate } from "react-router-dom";

const MainPage = () => {
  const navigate = useNavigate();
  const appName = "ssup!";

  return (
    <>
      <div className="main-container">
        <h3 className="main-title">
          지금 <span className="app-name">{appName}</span> 에서 새로운 친구를
          만나보세요! ✈️
        </h3>

        <div className="btn-group-row">
          <button className="community-btn" onClick={() => navigate("/posts")}>
            <span className="btn-text">커뮤니티 바로가기</span>
            <span className="fly-logo">✈️</span>
          </button>
          <button className="community-btn" onClick={() => navigate("/intro")}>
            <span className="btn-text">서비스 소개</span>
            <span className="fly-logo">🔖</span>
          </button>
        </div>

        <img src={mainImage2} alt="메인 이미지" className="main-image" />

        <div className="main-subtitle">
          <p>👬 근처에 있는 언어 교류 메이트를 찾아보세요!</p>
        </div>
        <div className="main-subtitle-children">
          <p>
            🔍 언어, 지역, 관심사 등 다양한 필터 검색을 통해 내가 원하는 친구를
            쉽게 찾을 수 있어요.
          </p>
          <p>
            🤖 친구 찾기 탭에서 내 프로필을 기반으로 AI가 추천해주는 친구를
            만나볼수도 있어요.
          </p>
        </div>

        <img src={mainImage3} alt="메인 이미지" className="main-image" />

        <div className="main-subtitle">
          <p>💫 마음에 드는 친구를 찾았다면, 매치 요청을 해보세요!</p>
        </div>

        <img src={mainImage1} alt="메인 이미지" className="main-image" />
      </div>
    </>
  );
};

export default MainPage;
