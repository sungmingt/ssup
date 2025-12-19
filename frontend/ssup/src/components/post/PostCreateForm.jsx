import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { fetchPosts } from "./../postList/PostApi.jsx";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import LanguageSelector from "./LanguageSelector";

const PostCreateForm = () => {
  const [usingLanguage, setUsingLanguage] = useState("");
  const [learningLanguage, setLearningLanguage] = useState("");

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [images, setImages] = useState([]);

  const [showUsingModal, setShowUsingModal] = useState(false);
  const [showLearningModal, setShowLearningModal] = useState(false);

  const navigate = useNavigate();

  //이미지 업로드
  const handleImageUpload = (e) => {
    setImages([...e.target.files]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!title.trim() || !content.trim()) {
      alert("제목과 내용을 입력해주세요.");
      return;
    }

    if (!usingLanguage || !learningLanguage) {
      alert("사용언어와 배우는 언어를 선택해주세요.");
      return;
    }

    const formData = new FormData();

    images.forEach((img) => formData.append("images", img));

    const dto = {
      usingLanguage,
      learningLanguage,
      title,
      content,
    };

    formData.append(
      "dto",
      new Blob([JSON.stringify(dto)], { type: "application/json" })
    );

    try {
      const res = await axios.post(
        "http://localhost:8080/api/posts",
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      );

      alert("글이 작성되었습니다.");
      navigate(`/posts/${res.data.id}`);
    } catch (error) {
      if (error.response?.status === 400) {
        alert("입력값을 다시 확인해주세요.");
      } else {
        alert("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
      }
    }
  };

  return (
    <>
      {/* <Header /> */}
      <div className="container py-5" style={{ maxWidth: "700px" }}>
        <h2 className="fw-bold mb-4 text-center">✏️ 글 작성</h2>

        <form onSubmit={handleSubmit}>
          {/* 사용언어 */}
          <LanguageSelector
            label="사용언어"
            value={usingLanguage}
            onSelect={setUsingLanguage}
          />

          {/* 배우는 언어 */}
          <LanguageSelector
            label="배우는 언어"
            value={learningLanguage}
            onSelect={setLearningLanguage}
          />

          {/* 제목 */}
          <div className="mb-3">
            <label className="form-label fw-bold">제목</label>
            <input
              className="form-control"
              placeholder="제목을 입력하세요"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
          </div>

          {/* 내용 */}
          <div className="mb-3">
            <label className="form-label fw-bold">내용</label>
            <textarea
              className="form-control"
              placeholder="내용을 입력하세요"
              rows={12}
              style={{
                resize: "vertical",
                padding: "15px",
                minHeight: "300px",
              }}
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
          </div>

          {/* 이미지 업로드 */}
          <div className="mb-3">
            <label className="form-label fw-bold">이미지 업로드</label>
            <input
              type="file"
              className="form-control"
              accept="image/*"
              multiple
              onChange={handleImageUpload}
            />
          </div>

          {/* 버튼 2개 */}
          <div className="d-flex gap-3 mt-4">
            <button
              className="btn btn-secondary w-50"
              type="button"
              onClick={() => navigate("/posts")}
            >
              취소하기
            </button>

            <button className="btn btn-primary w-50" type="submit">
              글 작성하기
            </button>
          </div>
        </form>
      </div>
    </>
  );
};

export default PostCreateForm;
