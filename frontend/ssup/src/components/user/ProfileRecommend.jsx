import { useEffect, useState } from "react";
import { recommendApi } from "@/api";
import ProfileCard from "./ProfileCard";
import { useAuthStore } from "@/store/authStore";

const ProfileRecommend = () => {
  const { user } = useAuthStore();

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!user?.id) return;

    loadRecommend(user.id);
  }, [user]);

  const loadRecommend = async (userId) => {
    setLoading(true);

    try {
      const res = await recommendApi.getRecommend(userId);
      setUsers(res.data);
    } catch (e) {
      console.error("추천 유저 불러오기 실패", e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container py-5">
      <h4 className="fw-bold mb-4">🔥 추천 친구</h4>

      <div className="row">
        {users.map((user) => (
          <ProfileCard key={user.id} user={user} />
        ))}
      </div>

      {loading && <p className="text-center mt-3 text-muted">불러오는 중...</p>}

      {!loading && users.length === 0 && (
        <p className="text-center mt-3">추천 결과가 없습니다.</p>
      )}
    </div>
  );
};

export default ProfileRecommend;
