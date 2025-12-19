import { useState, useEffect } from "react";
import axios from "axios";

const LanguageSelector = ({ label, value, onSelect }) => {
  const [showModal, setShowModal] = useState(false);
  const [languages, setLanguages] = useState([]);

  useEffect(() => {
    const fetchLanguages = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/languages");
        setLanguages(res.data); // [{ code, name }]
      } catch (err) {
        console.error("언어 정보 불러오기 실패", err);
      }
    };

    fetchLanguages();
  }, []);

  return (
    <>
      {/* 선택 버튼 */}
      <div className="mb-3">
        <label className="form-label fw-bold">{label}</label>
        <button
          type="button"
          className="btn btn-outline-secondary w-100"
          onClick={() => setShowModal(true)}
        >
          {value || `${label} 선택`}
        </button>
      </div>

      {/* 모달 */}
      {showModal && (
        <div
          className="modal fade show"
          style={{ display: "block", background: "rgba(0,0,0,0.45)" }}
        >
          <div className="modal-dialog">
            <div className="modal-content p-3">
              <div className="modal-header">
                <h5 className="modal-title">{label} 선택</h5>
                <button
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                />
              </div>

              <div className="modal-body">
                {languages.map((lang) => (
                  <button
                    key={lang.code} // ✅ 안정적인 key
                    className={`btn w-100 mb-2 ${
                      lang.name === value
                        ? "btn-primary"
                        : "btn-outline-secondary"
                    }`}
                    onClick={() => {
                      onSelect(lang.name); // ✅ 문자열만 전달
                      setShowModal(false);
                    }}
                  >
                    {lang.name} {/* ✅ 렌더링 */}
                  </button>
                ))}
              </div>

              <div className="modal-footer">
                <button
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  닫기
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default LanguageSelector;
