import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApi, profileApi } from "@/api";
import { languageApi } from "@/api";
import { matchApi } from "@/api/match.api";
import defaultProfile from "../../assets/ssup_user_default_image.png";
import "./../../css/Profile.css";
import InfoLayout from "./../../layouts/InfoLayout";
import { useAuthStore } from "@/store/authStore";
import { CONFIRM_MESSAGE } from "../common/confirmMessage";
import { useConfirmStore } from "@/store/confirmStore";

function Profile({ isMyProfile: isMyProfileProp = false }) {
  const { open } = useConfirmStore();
  const { user: me } = useAuthStore();
  const { id: urlId } = useParams();

  const { id } = useParams();
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [userLanguages, setUserLanguages] = useState(null);

  const isMyProfile =
    isMyProfileProp || (me && String(me.id) === String(urlId));

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
        const res = isMyProfile
          ? await languageApi.getMyLanguages()
          : await languageApi.getUserLanguages(id);

        setUserLanguages(res.data);
      } catch (e) {
        console.error("유저 언어 조회 실패", e);
      }
    };

    // isMyProfile이거나 id가 있을 때 실행
    if (isMyProfile || id) {
      fetchUserLanguages();
    }
  }, [id, isMyProfile]);

  //매치 버튼 렌더링
  const renderMatchButton = () => {
    if (isMyProfile) return null;

    const matchInfo = profile.matchInfoResponse;

    //매치 기록이 없는 경우
    if (
      !matchInfo ||
      !matchInfo.matchStatus ||
      matchInfo.matchStatus === "NONE"
    ) {
      return (
        <button className="btn btn-success btn-sm" onClick={onMatchRequest}>
          친구 요청
        </button>
      );
    }

    const { matchStatus, amIRequester } = matchInfo;

    switch (matchStatus) {
      case "ACCEPTED":
        return (
          <button className="btn btn-secondary btn-sm" disabled>
            ✔️ 매치됨
          </button>
        );

      case "PENDING":
        if (amIRequester) {
          //내가 보낸 경우
          return (
            <button className="btn btn-light btn-sm text-muted" disabled>
              매치 요청 대기 중
            </button>
          );
        } else {
          //상대가 보낸 경우
          return (
            <button
              className="btn btn-sm fw-bold accept-btn"
              onClick={() => navigate("/me/matches")}
            >
              매치 요청 수락하기
            </button>
          );
        }

      case "REJECTED":
        if (amIRequester) {
          //상대가 거절한 경우
          return (
            <button className="btn btn-light btn-sm text-muted" disabled>
              매치 요청 대기 중
            </button>
          );
        } else {
          //내가 거절한 경우
          return (
            <button className="btn btn-light btn-sm text-muted" disabled>
              매치 거절함
            </button>
          );
        }

      default:
        return (
          <button className="btn btn-success btn-sm" onClick={onMatchRequest}>
            친구 요청
          </button>
        );
    }
  };

  const onMatchRequest = async () => {
    if (!me) return alert("로그인 후 이용해주세요.");

    const dto = {
      receiverId: profile.id,
    };

    try {
      await matchApi.sendRequest(dto);

      setProfile((prev) => ({
        ...prev,
        matchInfoResponse: {
          matchStatus: "PENDING",
          amIRequester: true,
        },
      }));

      alert("친구 요청을 보냈습니다.");
    } catch (err) {
      const errorMsg =
        err.response?.data?.message || "친구 신청에 실패했습니다.";
      alert(errorMsg);
    }
  };

  {
    /* 계정 삭제 */
  }
  const onDeleteAccount = async () => {
    open(
      CONFIRM_MESSAGE.DELETE_USER(async () => {
        await authApi.quit();
        await useAuthStore.getState().clearAuth();
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
              {isMyProfile && (
                <button
                  className="btn btn-outline-secondary btn-sm profile-action-btn"
                  onClick={() => navigate("/me/profile/edit")}
                >
                  프로필 수정
                </button>
              )}

              {!isMyProfile && me && (
                <div className="profile-action-area">{renderMatchButton()}</div>
              )}
            </div>

            {/* 지역 */}
            <span className="profile-location">
              🏠 {profile.location.siDoName} · {profile.location.siGunGuName}
            </span>

            <p className="profile-intro">{profile.intro}</p>
          </div>
        </div>

        {/* 관심사 */}
        <div className="profile-section card">
          <h5>✨ 관심사</h5>
          <div className="interest-list">
            {profile.interests.map((i) => (
              <span key={i.id} className="interest-chip">
                #{i.name}
              </span>
            ))}
          </div>
        </div>

        {/* 연락처 (내 프로필 or 매치된 경우만) */}
        {isMyProfile || profile.matchInfoResponse.matchStatus === "ACCEPTED" ? (
          profile.contact && (
            <div className="profile-section card contact-card">
              <h5>💌 연락처</h5>
              <p className="contact-text">{profile.contact}</p>
            </div>
          )
        ) : (
          <div className="profile-section card contact-card">
            <h5>💌 연락처</h5>
            <small className="text-muted">
              매치되면 연락처를 볼 수 있습니다.
            </small>
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
