import { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import { guardAuthAction } from "@/components/common/guardAuthAction";

const PostTopBar = ({ sortType, setSortType }) => {
  const navigate = useNavigate();

  const onClickWrite = () => {
    guardAuthAction(() => navigate("/posts/create"));
  };

  return (
    <div className="container py-5">
      {/*상단 메뉴 - 가운데 정렬 */}
      <div className="d-flex justify-content-center align-items-center mb-4 gap-3">
        {/* 글쓰기 버튼 */}
        <button className="btn btn-success px-3" onClick={onClickWrite}>
          ✏️ 글쓰기
        </button>

        {/* 필터 버튼 */}
        <button
          className="btn btn-outline-secondary"
          style={{ width: "120px" }}
          data-bs-toggle="modal"
          data-bs-target="#filterModal" //id=filterModal인 요소를 찾도록 브라우저에게 명령 내림
        >
          ♻️ 필터
        </button>

        {/* 정렬 */}
        <select
          className="form-select"
          style={{ width: "120px" }}
          value={sortType}
          onChange={(e) => setSortType(e.target.value)}
        >
          <option value="LATEST">최신순</option>
          <option value="VIEWS">조회수순</option>
        </select>
      </div>
    </div>
  );
};

export default PostTopBar;
