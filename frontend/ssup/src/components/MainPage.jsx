import mainImage1 from "./../assets/main_logo_1.png";
import mainImage2 from "./../assets/main_logo_2.png";
import mainImage3 from "./../assets/main_logo_3.png";
import "@/css/MainPage.css";
import { useNavigate } from "react-router-dom";
import IntroPage from "@/components/IntroPage";

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

        <button className="community-btn" onClick={() => navigate("/posts")}>
          <span className="btn-text">커뮤니티 바로가기</span>
          <span className="fly-logo">✈️</span>
        </button>

        <img src={mainImage2} alt="메인 이미지" className="main-image" />
      </div>
      <div>
        <IntroPage />
      </div>
      <div className="main-container">
        <img src={mainImage3} alt="메인 이미지" className="main-image" />
        <img src={mainImage1} alt="메인 이미지" className="main-image" />
      </div>
    </>
  );
};

export default MainPage;
