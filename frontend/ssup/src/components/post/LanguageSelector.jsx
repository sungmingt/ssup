import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

const LANGUAGES = [
  "Korean",
  "English",
  "Spanish",
  "Japanese",
  "Chinese",
  "German",
  "French",
];

const LanguageSelector = ({ label, value, onSelect }) => {
  const [showModal, setShowModal] = useState(false);

  return (
    <>
      {/* 언어 선택 버튼 */}
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
          style={{
            display: "block",
            background: "rgba(0,0,0,0.45)",
          }}
        >
          <div className="modal-dialog">
            <div className="modal-content p-3">
              <div className="modal-header">
                <h5 className="modal-title">{label} 선택</h5>
                <button
                  className="btn-close"
                  onClick={() => setShowModal(false)}
                ></button>
              </div>

              <div className="modal-body">
                {LANGUAGES.map((lang) => (
                  <button
                    key={lang}
                    className={`btn w-100 mb-2 ${
                      lang === value ? "btn-primary" : "btn-outline-secondary"
                    }`}
                    onClick={() => {
                      onSelect(lang);
                      setShowModal(false);
                    }}
                  >
                    {lang}
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
