import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { commentApi } from "@/api";
import { CONFIRM_MESSAGE } from "../common/confirmMessage";
import { useConfirmStore } from "@/store/confirmStore";
import { useAuthStore } from "@/store/authStore";
import "bootstrap/dist/css/bootstrap.min.css";
import "@/css/comment/CommentItem.css";
import defaultProfile from "./../../assets/ssup_user_default_image.png";

const CommentItem = ({ comment, onRefresh, onEdit, authorId }) => {
  const { open } = useConfirmStore();
  const navigate = useNavigate();

  const { user } = useAuthStore();
  const [isMine, setIsMine] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const [hearted, setHearted] = useState(comment.hearted);
  const [heartCount, setHeartCount] = useState(comment.heartCount);
  const [heartLoading, setHeartLoading] = useState(false);

  useEffect(() => {
    if (user && user.id === authorId) {
      setIsMine(true);
    }
  }, [user, authorId]);

  console.log(authorId);
  const onDelete = () => {
    open(
      CONFIRM_MESSAGE.DELETE_COMMENT(async () => {
        await commentApi.deleteComment(comment.postId, comment.id);
        onRefresh();
      })
    );
  };

  const onToggleHeart = async () => {
    if (heartLoading) return;

    const previousHearted = hearted;
    const previousCount = heartCount;

    setHearted(!hearted);
    setHeartCount((prev) => (hearted ? prev - 1 : prev + 1));

    setHeartLoading(true);

    try {
      const res = await commentApi.toggleHeart(comment.id);

      setHearted(res.data.hearted);
      setHeartCount(res.data.heartCount);
    } catch {
      setHearted(previousHearted);
      setHeartCount(previousCount);
      alert("요청이 너무 많아 처리에 실패했습니다.");
    } finally {
      setHeartLoading(false);
    }
  };

  return (
    <div className="comment-card shadow-sm rounded p-3">
      <div className="d-flex">
        {/* 프로필 영역 (고정 폭) */}
        <div className="me-1">
          <img
            src={comment.authorImageUrl || defaultProfile}
            alt="author"
            className="rounded-circle author-btn"
            style={{ width: 40, height: 40, objectFit: "cover" }}
            onClick={() => navigate(`/users/${authorId}/profile`)}
          />
        </div>

        {/* 본문 영역 */}
        <div className="flex-grow-1">
          {/* 상단: 이름 + 수정/삭제 */}
          <div className="d-flex justify-content-between align-items-center mb-1">
            <span
              className="fw-semibold author-btn"
              onClick={() => navigate(`/users/${authorId}/profile`)}
            >
              {comment.authorName}
            </span>

            {isMine && (
              <div className="d-flex gap-2">
                <button
                  className="btn btn-sm btn-outline-secondary"
                  onClick={onEdit}
                >
                  수정
                </button>
                <button
                  className="btn btn-sm btn-outline-danger"
                  onClick={onDelete}
                  disabled={deleting}
                >
                  삭제
                </button>
              </div>
            )}
          </div>

          {/* 댓글 내용 */}
          <p className="mb-2">{comment.content}</p>

          {comment.imageUrl && (
            <img
              src={comment.imageUrl}
              alt="comment"
              className="img-fluid rounded mb-2"
              style={{ maxHeight: 200 }}
            />
          )}

          {/*작성일 + 좋아요 */}
          <div className="d-flex justify-content-between text-muted small">
            <span>{new Date(comment.createdAt).toLocaleString()}</span>
            <span
              onClick={onToggleHeart}
              style={{
                cursor: heartLoading ? "default" : "pointer",
                userSelect: "none",
              }}
            >
              {hearted ? "❤️" : "🤍"} {heartCount}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CommentItem;
