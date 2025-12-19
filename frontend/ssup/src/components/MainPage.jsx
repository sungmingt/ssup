import mainImage from "./../assets/ssup_main_image.png";
import "./../css/MainPage.css";
import { useNavigate } from "react-router-dom";

const MainPage = () => {
  const navigate = useNavigate();

  return (
    <>
      <div className="main-container">
        <img src={mainImage} alt="메인 페이지" className="main-image" />

        <h1 className="main-title">
          지금, Suup!에서 새로운 친구를 만나보세요 🌍
        </h1>
        <p className="main-subtitle"></p>

        {/* 커뮤니티 바로가기 버튼 */}

        <button className="community-btn" onClick={() => navigate("/posts")}>
          커뮤니티 바로가기
        </button>
      </div>
    </>
  );
};

export default MainPage;
