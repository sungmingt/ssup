import mainImage from "./../assets/ssup_logo_2.png";
// import mainImage3 from "./../assets/ssup_main_image_3.png";
import "./../css/MainPage.css";
import { useNavigate } from "react-router-dom";
import InfoLayout from "@/layouts/InfoLayout";

const MainPage = () => {
  const navigate = useNavigate();

  return (
    <>
      <div className="main-container">
        <h1 className="main-title">
          지금 ssup! 에서 새로운 친구를 만나보세요 ✈️
        </h1>

        <img src={mainImage} alt="메인 이미지" className="main-image" />

        {/* <h2 className="main-subtitle"> */}
        {/* 근처에 있는 언어 교류 메이트를 찾아보세요! */}
        {/* </h2> */}

        {/* <img src={mainImage3} alt="메인 이미지3" className="main-image-3" /> */}

        <button className="community-btn" onClick={() => navigate("/posts")}>
          커뮤니티 바로가기
        </button>
      </div>
    </>
  );
};

export default MainPage;
