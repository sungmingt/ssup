import { useNavigate } from "react-router-dom";
import "../../css/PostCard.css";
import defaultProfile from "../../assets/ssup_user_default_image.png";
import defaultThumbnail from "../../assets/ssup_post_default_image.webp";

const PostCard = ({ post }) => {
  const navigate = useNavigate();

  if (!post) return null;

  const thumbnail = post.thumbnailImageUrl || defaultThumbnail;
  const createdAt = post.createdAt
    ? new Date(post.createdAt).toLocaleDateString()
    : "";

  return (
    <div className="col-12 col-sm-6 col-md-4 mb-4">
      <div
        className="card h-100 shadow-sm post-card"
        style={{ cursor: "pointer" }}
        onClick={() => navigate(`/posts/${post.id}`)}
      >
        {/* 썸네일 */}
        <div className="post-thumbnail-wrapper">
          <img
            src={thumbnail}
            alt="thumbnail"
            className="post-thumbnail"
            onError={(e) => (e.target.src = defaultThumbnail)}
          />
        </div>

        <div className="card-body d-flex flex-column">
          {/* 제목 */}
          <div className="d-flex justify-content-between align-items-start mb-1 gap-2">
            <h5 className="card-title fw-bold post-title mb-0">
              {post.title?.length > 30
                ? post.title.slice(0, 30) + "..."
                : post.title}
            </h5>

            {/* 언어 */}
            {(post.usingLanguage || post.learningLanguage) && (
              <div className="language-badge">
                {post.usingLanguage || "—"} → {post.learningLanguage || "—"}
              </div>
            )}
          </div>

          {/* 작성일 */}
          {post.createdAt && (
            <div className="text-muted small mb-2">
              {new Date(post.createdAt).toLocaleDateString()}
            </div>
          )}

          {/* 내용 */}
          <p className="card-text text-muted post-content">{post.content}</p>

          {/* 작성자 + 메타 */}
          <div className="d-flex align-items-center justify-content-between mt-auto pt-2">
            {/* 작성자 */}
            <div className="d-flex align-items-center">
              <img
                src={post.authorImageUrl || defaultProfile}
                alt="author"
                className="rounded-circle"
                style={{ width: 36, height: 36, objectFit: "cover" }}
              />
              <span className="fw-semibold ms-2">{post.authorName}</span>
            </div>

            {/* ❤️ 💬 👁️ */}
            <div className="d-flex gap-3 text-muted small">
              <span>❤️ {post.heartCount ?? 0}</span>
              <span>💬 {post.commentCount ?? 0}</span>
              <span>👁️ {post.viewCount ?? 0}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PostCard;
