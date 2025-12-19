import { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

const SearchModal = ({ onSearch }) => {
  const [location, setLocation] = useState("");
  const [language, setLanguage] = useState("");
  const [interest, setInterest] = useState("");

  const handleSearchClick = () => {
    if (onSearch) {
      onSearch({ location, language, interest });
    }
  };

  return (
    <div
      className="modal fade"
      id="searchModal"
      tabIndex="-1"
      aria-hidden="true"
    >
      <div className="modal-dialog modal-dialog-centered">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">검색하기</h5>
            <button className="btn-close" data-bs-dismiss="modal"></button>
          </div>

          <div className="modal-body">
            {/* 위치 */}
            <div className="mb-3">
              <label className="form-label fw-semibold">📍 위치</label>
              <input
                type="text"
                className="form-control"
                placeholder="예: 서울, 강남구"
                value={location}
                onChange={(e) => setLocation(e.target.value)}
              />
            </div>

            {/* 언어 */}
            <div className="mb-3">
              <label className="form-label fw-semibold">🗣️ 사용 언어</label>
              <input
                type="text"
                className="form-control"
                placeholder="예: 한국어, 영어"
                value={language}
                onChange={(e) => setLanguage(e.target.value)}
              />
            </div>

            {/* 관심사 */}
            <div className="mb-3">
              <label className="form-label fw-semibold">✨ 관심사</label>
              <input
                type="text"
                className="form-control"
                placeholder="예: 음악, 여행, 요리"
                value={interest}
                onChange={(e) => setInterest(e.target.value)}
              />
            </div>
          </div>

          <div className="modal-footer">
            <button className="btn btn-secondary" data-bs-dismiss="modal">
              닫기
            </button>

            <button
              className="btn btn-primary"
              data-bs-dismiss="modal"
              onClick={handleSearchClick}
            >
              검색 실행
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchModal;
