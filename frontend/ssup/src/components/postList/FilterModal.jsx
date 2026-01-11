import { useState, useEffect, useMemo } from "react";
import { usePostFilterStore } from "@/store/postFilterStore";
import "bootstrap/dist/css/bootstrap.min.css";
import "@/css/post/FilterModal.css";

const FilterModal = ({ filters, onFilter }) => {
  const { locations, languages, interests, isLoaded, fetchFilterMetadata } =
    usePostFilterStore();

  //선택된 시/도
  const [selectedParent, setSelectedParent] = useState(null);

  const [tempFilters, setTempFilters] = useState({
    locationId: null,
    usingLanguage: null,
    learningLanguage: null,
    interestId: null,
  });

  useEffect(() => {
    fetchFilterMetadata();
  }, [fetchFilterMetadata]);

  useEffect(() => {
    if (filters) {
      Promise.resolve().then(() => {
        setTempFilters(filters);

        //부모 컴포넌트는 군/구 정보밖에 모르기때문에, 부모 지역도 id를 찾아서 선택 상태로 변경
        if (filters.locationId && locations.length > 0) {
          const parent = locations.find((p) =>
            p.children.some((c) => c.id === filters.locationId)
          );

          if (parent) setSelectedParent(parent.id);
        } else if (!filters.locationId) {
          setSelectedParent(null);
        }
      });
    }
  }, [filters, locations]);

  //시/도 필드로 하위 리스트(시/군/구)를 가지고 있는 구조
  const parentLocations = locations;

  //선택된 시/도의 children 배열 로드
  const childLocations = useMemo(() => {
    if (!selectedParent) return [];

    const parent = locations.find((loc) => loc.id === selectedParent);
    return parent ? parent.children : [];
  }, [locations, selectedParent]);

  const handleParentSelect = (locId) => {
    const isReSelect = locId === selectedParent;
    const nextParentId = isReSelect ? null : locId;

    setSelectedParent(nextParentId);

    setTempFilters((prev) => ({ ...prev, locationId: nextParentId }));
  };

  const toggleSelect = (key, value) => {
    setTempFilters((prev) => ({
      ...prev,
      [key]: prev[key] === value ? null : value,
    }));
  };

  {
    /* 필터 초기화 */
  }
  const handleReset = () => {
    const initialFilters = {
      locationId: null,
      usingLanguage: null,
      learningLanguage: null,
      interestId: null,
    };

    //모달 내부 선택 초기화
    setTempFilters(initialFilters);
    setSelectedParent(null);

    //필터 초기화 부모에게 즉시 전달+렌더링
    onFilter(initialFilters);
  };

  if (!isLoaded) return null;

  return (
    <div
      className="modal fade"
      id="filterModal"
      tabIndex="-1"
      aria-hidden="true"
    >
      <div className="modal-dialog modal-dialog-centered modal-dialog-scrollable">
        <div className="modal-content filter-modal-content">
          <div className="modal-header">
            <h5 className="modal-title fw-bold">♻️ 상세 필터</h5>
            <button className="btn-close" data-bs-dismiss="modal"></button>
          </div>

          <div className="modal-body">
            {/* 시/도 선택 */}
            <h6 className="fw-bold">📍 지역 (시/도)</h6>
            <div className="d-flex flex-wrap gap-2">
              {parentLocations.map((loc) => (
                <span
                  key={loc.id}
                  className={`interest-chip ${
                    selectedParent === loc.id ? "active" : ""
                  }`}
                  style={{ cursor: "pointer" }}
                  onClick={() => handleParentSelect(loc.id)}
                >
                  {loc.name}
                </span>
              ))}
            </div>

            {/* 군/구 선택 (시/도가 선택되었을 때만 노출) */}
            {selectedParent && (
              <div className="p-3 rounded border-start border-4 border-primary bg-light">
                <h6
                  className="fw-boldtext-primary"
                  style={{ fontSize: "0.9rem" }}
                >
                  ┕ {parentLocations.find((p) => p.id === selectedParent)?.name}{" "}
                  상세 지역
                </h6>
                <div className="d-flex flex-wrap gap-2">
                  {childLocations.map((child) => (
                    <span
                      key={child.id}
                      className={`interest-chip ${
                        tempFilters.locationId === child.id ? "active" : ""
                      }`}
                      onClick={() => toggleSelect("locationId", child.id)}
                    >
                      {child.name}
                    </span>
                  ))}
                </div>
              </div>
            )}

            {/* 사용 언어 */}
            <h6 className="fw-bold">🗣️ 사용 언어</h6>
            <div className="d-flex flex-wrap gap-2">
              {languages.map((lang) => (
                <span
                  key={lang.id}
                  className={`interest-chip ${
                    tempFilters.usingLanguage === lang.name ? "active" : ""
                  }`}
                  onClick={() => toggleSelect("usingLanguage", lang.name)}
                >
                  {lang.name}
                </span>
              ))}
            </div>

            {/* 학습 언어 */}
            <h6 className="fw-bold">📚 학습 언어</h6>
            <div className="d-flex flex-wrap gap-2">
              {languages.map((lang) => (
                <span
                  key={lang.id}
                  className={`interest-chip ${
                    tempFilters.learningLanguage === lang.name ? "active" : ""
                  }`}
                  onClick={() => toggleSelect("learningLanguage", lang.name)}
                >
                  {lang.name}
                </span>
              ))}
            </div>
            {/* 관심사 */}
            <h6 className="fw-bold">✨ 관심사</h6>
            <div className="d-flex flex-wrap gap-2">
              {interests.map((it) => (
                <span
                  key={it.id}
                  className={`interest-chip ${
                    tempFilters.interestId === it.id ? "active" : ""
                  }`}
                  onClick={() => toggleSelect("interestId", it.id)}
                >
                  {it.name}
                </span>
              ))}
            </div>
          </div>

          <div className="modal-footer filter-modal-footer">
            <button
              className="btn btn-primary filter-apply-btn"
              data-bs-dismiss="modal"
              onClick={() => onFilter(tempFilters)}
            >
              ♻️ 필터 적용
            </button>

            <button
              type="button"
              className="btn btn-outline-danger filter-reset-btn"
              onClick={handleReset}
              data-bs-dismiss="modal" //클릭 시 모달 닫음
            >
              🔄 필터 초기화
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FilterModal;
