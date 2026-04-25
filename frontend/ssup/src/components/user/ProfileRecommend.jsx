import { useEffect, useState } from "react";
import { recommendApi } from "@/api";
import ProfileCard from "./ProfileCard";
import "@/css/user/ProfileRecommend.css";
import { useAuthStore } from "@/store/authStore";

const ProfileRecommend = () => {
  const { user } = useAuthStore();

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadRecommend();
  }, [user]);

  const loadRecommend = async () => {
    setLoading(true);

    try {
      let res;

      if (user?.id) {
        //로그인: 추천 유저
        res = await recommendApi.getRecommend(user.id);
      } else {
        //비로그인: 랜덤 유저
        res = await recommendApi.getRandomUsers();
      }

      setUsers(res.data || []);
    } catch (e) {
      console.error("유저 불러오기 실패", e);
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  //설명 문구 분기
  const description = user?.id
    ? "회원님의 언어, 관심사, 지역 등의 정보를 기반으로 AI가 추천해준 친구 목록이에요!"
    : "서비스를 이용 중인 랜덤 유저 목록입니다. 로그인 후에는 AI가 나와 잘 맞는 친구를 추천해드려요!";

  return (
    <div className="page-wrapper">
      <div className="container py-5">
        <div className="text-center">
          <h4 className="fw-bold mb-3">🙋‍♀️ 추천 친구</h4>
          <p className="mb-5 text-muted small">{description}</p>
        </div>

        <div className="row">
          {users.map((user) => (
            <ProfileCard key={user.id} user={user} />
          ))}
        </div>

        {loading && (
          <p className="text-center mt-3 text-muted">불러오는 중...</p>
        )}

        {!loading && users.length === 0 && (
          <p className="text-center mt-3">추천 결과가 없습니다.</p>
        )}
      </div>
    </div>
  );
};

export default ProfileRecommend;
