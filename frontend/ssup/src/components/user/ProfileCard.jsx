import { useNavigate } from "react-router-dom";
import defaultProfile from "@/assets/ssup_user_default_image.png";
import "./../../css/user/ProfileCard.css";

const ProfileCard = ({ user }) => {
  const navigate = useNavigate();

  return (
    <div className="col-md-4 mb-4">
      <div
        className="card profile-card h-100 shadow-sm"
        onClick={() => navigate(`/users/${user.id}/profile`)}
        style={{ cursor: "pointer" }}
      >
        <div className="card-body">
          {/* 프로필 */}
          <div className="d-flex align-items-center mb-3">
            <img
              src={user.imageUrl || defaultProfile}
              className="rounded-circle me-3 profile-image"
              width="56"
              height="56"
            />

            <div>
              <h6 className="mb-0 fw-bold profile-name">{user.nickname}</h6>
              <small className="text-muted">{user.age}세</small>
            </div>
          </div>

          {/* 언어 */}
          <div className="mb-2">
            <span className="language-badge">
              {user.usingLanguages?.join(", ") || "—"}
              <span className="lang-arrow"> → </span>
              {user.learningLanguages?.join(", ") || "—"}
            </span>
          </div>

          {/* 위치 */}
          <div className="text-muted small profile-location">
            📍 {user.location?.siDoName} · {user.location?.siGunGuName}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProfileCard;
