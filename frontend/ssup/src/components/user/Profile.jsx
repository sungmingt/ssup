import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { profileApi } from "@/api";
import { languageApi } from "@/api";
import defaultProfile from "../../assets/ssup_user_default_image.png";
import "./../../css/Profile.css";
import InfoLayout from "./../../layouts/InfoLayout";
import { useAuthStore } from "@/store/authStore";
import { CONFIRM_MESSAGE } from "../common/confirmMessage";
import { useConfirmStore } from "@/store/confirmStore";

function Profile({ isMyProfile = false }) {
  const { open } = useConfirmStore();

  const { id } = useParams();
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [userLanguages, setUserLanguages] = useState(null);

  {
    /* 유저의 프로필 조회 */
  }
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        if (isMyProfile) {
          const res = await profileApi.getMyProfile();
          setProfile(res.data);
          return;
        }

        //타인 프로필일때, id 없으면 호출 금지
        if (!id) return;

        const res = await profileApi.getUserProfile(id);
        setProfile(res.data);
      } catch (e) {
        console.error("프로필 조회 실패", e);
      }
    };

    fetchProfile();
  }, [id, isMyProfile]);

  {
    /* 유저의 언어 조회 */
  }
  useEffect(() => {
    const fetchUserLanguages = async () => {
      try {
        if (!id) return;

        const res = await languageApi.getUserLanguages(id);
        setUserLanguages(res.data);
      } catch (e) {
        console.error("유저 언어 조회 실패", e);
      }
    };

    fetchUserLanguages();
  }, [id]);

  {
    /* 계정 삭제 */
  }
  const onDeleteAccount = async () => {
    open(
      CONFIRM_MESSAGE.DELETE_USER(async () => {
        await profileApi.deleteMyProfile();
        navigate("/");
      })
    );
  };

  if (!profile) return <div className="text-center mt-5">로딩중...</div>;

  return (
    <InfoLayout>
      <div className="profile-wrapper">
        {/* 상단 프로필 카드 */}
        <div className="profile-header card">
          <div className="profile-avatar-wrapper">
            <img
              src={profile.imageUrl || defaultProfile}
              alt="avatar"
              className="profile-avatar"
            />
          </div>

          {/* 이름, 언어 */}
          <div className="profile-info">
            <div className="profile-title-row">
              <div className="profile-name-group">
                <h4 className="profile-name">{profile.nickname}</h4>

                {userLanguages && (
                  <span className="language-badge profile-inline-language">
                    {userLanguages.usingLanguages
                      ?.map((x) => x.name)
                      .join(", ") || "—"}
                    <span className="lang-arrow">→</span>
                    {userLanguages.learningLanguages
                      ?.map((x) => x.name)
                      .join(", ") || "—"}
                  </span>
                )}
              </div>

              {/* 친구 요청 / 수정 버튼 */}
              {isMyProfile ? (
                <button
                  className="btn btn-outline-secondary btn-sm profile-action-btn"
                  onClick={() => navigate("/me/profile/edit")}
                >
                  프로필 수정
                </button>
              ) : (
                <button className="btn btn-success btn-sm profile-action-btn">
                  친구 요청
                </button>
              )}
            </div>

            {/* 지역 */}
            <span className="profile-location">
              {profile.location.siDoName} · {profile.location.siGunGuName}
            </span>

            <p className="profile-intro">{profile.intro}</p>
          </div>
        </div>

        {/* 관심사 */}
        <div className="profile-section card">
          <h5>관심사</h5>
          <div className="interest-list">
            {profile.interests.map((i) => (
              <span key={i.id} className="interest-chip">
                #{i.name}
              </span>
            ))}
          </div>
        </div>

        {/* 연락처 (내 프로필 or 매치된 경우만) */}
        {profile.contact && (
          <div className="profile-section card contact-card">
            <h5>연락처</h5>
            <p>{profile.contact}</p>
          </div>
        )}

        {isMyProfile && (
          <div className="mt-3 text-end">
            <button
              type="button"
              className="btn account-delete-btn profile-delete-btn"
              onClick={onDeleteAccount}
            >
              계정 삭제
            </button>
          </div>
        )}
      </div>
    </InfoLayout>
  );
}

export default Profile;
