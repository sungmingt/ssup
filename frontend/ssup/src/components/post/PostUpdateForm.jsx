import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import LanguageSelector from "./LanguageSelector";

const API_BASE_URL = "http://localhost:8080";

const PostUpdateForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [usingLanguage, setUsingLanguage] = useState("");
  const [learningLanguage, setLearningLanguage] = useState("");

  const [existingImages, setExistingImages] = useState([]);
  const [addedImages, setAddedImages] = useState([]);

  const [loading, setLoading] = useState(true);

  // 1️⃣ 기존 글 데이터 불러오기
  useEffect(() => {
    const fetchPost = async () => {
      try {
        const res = await axios.get(`${API_BASE_URL}/api/posts/${id}`);
        const post = res.data;

        setTitle(post.title ?? ""); //초기 렌더링 시 controlled state로 생성
        setContent(post.content ?? "");
        setUsingLanguage(post.usingLanguage);
        setLearningLanguage(post.learningLanguage);
        setExistingImages(post.imageUrls || []);
      } catch (e) {
        alert("게시글을 불러오지 못했습니다.");
      } finally {
        setLoading(false);
      }
    };

    fetchPost();
  }, [id]);

  // 2️⃣ 새 이미지 추가
  const onAddImages = (e) => {
    setAddedImages([...e.target.files]);
  };

  // 3️⃣ 기존 이미지 삭제
  const onRemoveExistingImage = (url) => {
    setExistingImages((prev) => prev.filter((img) => img !== url));
  };

  // 4️⃣ 수정 제출
  const onSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    addedImages.forEach((img) => formData.append("addedImages", img));

    const dto = {
      title,
      content,
      usingLanguage,
      learningLanguage,
      keepImageUrls: existingImages,
    };

    formData.append(
      "dto",
      new Blob([JSON.stringify(dto)], { type: "application/json" })
    );

    try {
      await axios.put(`http://localhost:8080/api/posts/${id}`, formData);
      alert("게시글이 수정되었습니다.");
      navigate(`/posts/${id}`);
    } catch (e) {
      alert("게시글 수정에 실패했습니다.");
    }
  };

  if (loading) return <p className="text-center mt-5">불러오는 중...</p>;

  return (
    <div className="container py-5" style={{ maxWidth: 700 }}>
      <h2 className="fw-bold mb-4 text-center">✏️ 글 수정</h2>

      <form onSubmit={onSubmit}>
        <LanguageSelector
          label="사용 언어"
          value={usingLanguage}
          onSelect={setUsingLanguage}
        />
        <LanguageSelector
          label="배우는 언어"
          value={learningLanguage}
          onSelect={setLearningLanguage}
        />

        <input
          className="form-control mb-3"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <textarea
          className="form-control mb-3"
          rows={10}
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />

        {/* 기존 이미지 */}
        {existingImages.length > 0 && (
          <div className="mb-3">
            <label className="fw-bold">기존 이미지</label>
            <div className="d-flex flex-wrap gap-2">
              {existingImages.map((url) => (
                <div key={url} style={{ position: "relative" }}>
                  <img
                    src={url}
                    style={{ width: 100, height: 100, objectFit: "cover" }}
                  />
                  <button
                    type="button"
                    className="btn btn-sm btn-danger"
                    style={{ position: "absolute", top: 0, right: 0 }}
                    onClick={() => onRemoveExistingImage(url)}
                  >
                    ×
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 새 이미지 */}
        <input
          type="file"
          multiple
          className="form-control mb-4"
          onChange={onAddImages}
        />

        <div className="d-flex gap-3">
          <button
            type="button"
            className="btn btn-secondary w-50"
            onClick={() => navigate(-1)}
          >
            취소
          </button>
          <button type="submit" className="btn btn-primary w-50">
            수정 완료
          </button>
        </div>
      </form>
    </div>
  );
};

export default PostUpdateForm;
