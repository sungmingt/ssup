import { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import "@/css/post/PostTopBar.css";
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
        <button
          className="btn btn-success px-3 top-control"
          onClick={onClickWrite}
        >
          ✏️ 글쓰기
        </button>

        <button
          className="btn btn-outline-secondary top-control"
          data-bs-toggle="modal"
          data-bs-target="#filterModal"
        >
          ♻️ 필터
        </button>

        <select
          className="form-select top-control"
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
