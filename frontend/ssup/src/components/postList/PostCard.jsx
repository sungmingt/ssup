import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "../../css/PostCard.css";
import defaultProfile from "../../assets/ssup_user_default_image.png";
import defaultThumbnail from "../../assets/ssup_post_default_image.webp";

const PostCard = ({ post }) => {
  const navigate = useNavigate();
  const profileImage = post.authorImageUrl || defaultProfile;
  const thumbnail = post.thumbnailImageUrl || defaultThumbnail;

  if (!post) return null;

  return (
    <div className="col-12 col-sm-6 col-md-4 mb-4">
      <div
        className="card h-100 shadow-sm post-card"
        style={{ cursor: "pointer" }}
        onClick={() => navigate(`/posts/${post.id}`)}
      >
        {/* 🔹 썸네일 */}
        <div className="post-thumbnail-wrapper">
          <img
            src={thumbnail}
            alt="thumbnail"
            className="post-thumbnail"
            onError={(e) => {
              e.target.src = defaultThumbnail;
            }}
          />
        </div>

        {/* 🔹 카드 본문 */}
        <div className="card-body d-flex flex-column">
          {(post.usingLanguage || post.learningLanguage) && (
            <small className="text-muted mb-1">
              {post.usingLanguage || "—"} → {post.learningLanguage || "—"}
            </small>
          )}

          <h5 className="card-title fw-bold post-title">
            {post.title?.length > 30
              ? post.title.slice(0, 30) + "..."
              : post.title}
          </h5>

          <p className="card-text text-muted post-content">{post.content}</p>

          {/* 작성자 */}
          <div className="d-flex align-items-center mt-auto pt-2">
            <img
              src={post.authorImageUrl || defaultProfile}
              alt="author"
              className="rounded-circle"
              style={{
                width: "36px",
                height: "36px",
                objectFit: "cover",
              }}
            />
            <span className="fw-semibold ms-2">{post.authorName}</span>
          </div>

          {/* 메타 */}
          <div className="d-flex justify-content-between mt-3 text-muted small">
            <span>❤️ {post.heartCount ?? 0}</span>
            <span>💬 {post.commentCount ?? 0}</span>
            <span>👁️ {post.viewCount ?? 0}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PostCard;
