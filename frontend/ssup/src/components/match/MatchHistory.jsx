import React, { useEffect, useState } from "react";
import { matchApi } from "@/api";
import InfoLayout from "@/layouts/InfoLayout";
import defaultProfile from "@/assets/ssup_user_default_image.png";
import { useNavigate } from "react-router-dom";
import "@/css/match/MatchHistory.css";

const MatchHistory = () => {
  const [matches, setMatches] = useState([]);
  const [activeTab, setActiveTab] = useState("RECEIVED");

  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchMatches();
  }, []);

  const fetchMatches = async () => {
    try {
      const res = await matchApi.getMyMatches();
      setMatches(res.data);
    } catch (err) {
      console.error("매치 내역 조회 실패", err);
    } finally {
      setLoading(false);
    }
  };

  const filteredMatches = matches.filter((m) => m.matchType === activeTab);

  if (loading) return <div className="text-center mt-5">로딩 중...</div>;

  return (
    <InfoLayout>
      <div className="match-history-wrapper py-5">
        <div className="mb-4">
          <h3 className="fw-bold">매치 관리</h3>
          <p className="text-muted small">
            받은 매치 요청과 보낸 매치 요청이 표시됩니다.
          </p>
        </div>

        {/* 탭 메뉴 */}
        <div className="match-tab-group">
          <button
            className={`match-tab-btn ${
              activeTab === "RECEIVED" ? "active" : ""
            }`}
            onClick={() => setActiveTab("RECEIVED")}
          >
            받은 요청
          </button>
          <button
            className={`match-tab-btn ${
              activeTab === "REQUESTED" ? "active" : ""
            }`}
            onClick={() => setActiveTab("REQUESTED")}
          >
            보낸 신청
          </button>
        </div>

        {/* 매치 내역 */}
        <div className="match-list-container">
          {filteredMatches.length === 0 ? (
            <div className="match-card justify-content-center text-muted py-5">
              내역이 없습니다.
            </div>
          ) : (
            filteredMatches.map((match) => (
              <div key={match.id} className="match-card">
                <div
                  className="match-partner-info"
                  onClick={() => navigate(`/users/${match.partnerId}/profile`)}
                >
                  <img
                    src={match.partnerImageUrl || defaultProfile}
                    className="match-partner-img"
                    alt="partner"
                  />
                  <div className="ms-3">
                    <div className="match-partner-name">
                      {match.partnerName}
                    </div>
                    <div className="match-date">
                      {new Date(match.createdAt).toLocaleDateString()}
                    </div>
                  </div>
                </div>

                <div className="match-actions">
                  {match.matchStatus === "PENDING" ? (
                    match.matchType === "RECEIVED" ? (
                      <div className="d-flex gap-2">
                        <button className="btn btn-sm btn-outline-danger px-3">
                          거절
                        </button>
                        <button className="btn btn-sm btn-match-accept px-3">
                          수락
                        </button>
                      </div>
                    ) : (
                      <span className="match-status-badge">답변 대기 중</span>
                    )
                  ) : (
                    <span
                      className={`match-status-badge ${
                        match.matchStatus === "ACCEPTED" ? "accepted" : ""
                      }`}
                    >
                      {match.matchStatus === "ACCEPTED"
                        ? "✔️ 매치 완료"
                        : "거절됨"}
                    </span>
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </InfoLayout>
  );
};

export default MatchHistory;
