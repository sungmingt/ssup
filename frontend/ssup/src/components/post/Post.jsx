import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { postApi } from "@/api";
import { matchApi } from "@/api/match.api";
import "bootstrap/dist/css/bootstrap.min.css";
import "./../../css/post/Post.css";
import defaultProfile from "../../assets/ssup_user_default_image.png";
import defaultImage from "./../../assets/ssup_post_default_image.webp";
import CommentSection from "./../comment/CommentSection.jsx";
import InfoLayout from "./../../layouts/InfoLayout";
import { CONFIRM_MESSAGE } from "@/components/common/confirmMessage";
import { useConfirmStore } from "@/store/confirmStore";
import { useAuthStore } from "@/store/authStore";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const Post = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { open } = useConfirmStore();

  const { user, isAuthenticated } = useAuthStore();

  const [post, setPost] = useState(null);
  const [hearted, setHearted] = useState(false);
  const [heartCount, setHeartCount] = useState(0);

  const [loading, setLoading] = useState(true);

  const isMyPost =
    isAuthenticated && user?.id && post?.authorId && user.id === post.authorId;

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const res = await postApi.getPost(id);
        setPost(res.data);
      } catch (err) {
        console.error("게시글 상세 조회 실패", err);
      } finally {
        setLoading(false);
      }
    };

    fetchPost();
  }, [id]);

  useEffect(() => {
    if (!post) return;

    setHearted(post.heartedByMe);
    setHeartCount(post.heartCount);
  }, [post]);

  const onToggleHeart = async () => {
    const prevHearted = hearted;
    const prevCount = heartCount;

    setHearted(!hearted);
    setHeartCount((c) => (hearted ? c - 1 : c + 1));

    try {
      const res = await postApi.toggleHeart(post.id);
      setHearted(res.data.hearted);
      setHeartCount(res.data.heartCount);
    } catch {
      setHearted(prevHearted);
      setHeartCount(prevCount);
    }
  };

  //매치 버튼 렌더링
  const renderMatchButton = () => {
    if (isMyPost || !isAuthenticated || !user) return null;

    const matchInfo = post.matchInfoResponse;

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
    const dto = {
      receiverId: post.authorId,
    };

    try {
      await matchApi.sendRequest(dto);
      alert("친구 신청을 보냈습니다.");

      setPost((prev) => ({
        ...prev,
        matchInfoResponse: {
          matchStatus: "PENDING",
          amIRequester: true,
        },
      }));
    } catch (err) {
      const errorMsg =
        err.response?.data?.message || "친구 신청에 실패했습니다.";
      alert(errorMsg);
    }
  };

  const onDelete = async () => {
    open(
      CONFIRM_MESSAGE.DELETE_POST(async () => {
        await postApi.deletePost(post.id);
        navigate("/posts");
      })
    );
  };

  if (loading) {
    return <p className="text-center mt-5">불러오는 중...</p>;
  }

  if (!post) {
    return <p className="text-center mt-5">게시글을 찾을 수 없습니다.</p>;
  }

  const createdAt = new Date(post.createdAt);
  const createdText = isNaN(createdAt)
    ? "날짜 없음"
    : createdAt.toLocaleString();

  return (
    <InfoLayout>
      <div className="post-page-wrapper py-5">
        <div className="post-detail-wrapper mx-auto">
          {/* 뒤로가기 */}
          {/* 상단 네비게이션 + 수정/삭제 */}
          <div className="d-flex justify-content-between align-items-center mb-4">
            <button
              className="btn btn-sm btn-outline-secondary"
              onClick={() => navigate("/posts")}
            >
              ← 목록으로
            </button>

            {isMyPost && (
              <div className="d-flex gap-2">
                <button
                  className="btn btn-outline-secondary btn-sm"
                  onClick={() => navigate(`/posts/${post.id}/update`)}
                >
                  수정
                </button>
                <button
                  className="btn btn-outline-danger btn-sm"
                  onClick={onDelete}
                >
                  삭제
                </button>
              </div>
            )}
          </div>

          {/* 메인 카드 */}
          <div className="post-main-card bg-white shadow-sm rounded p-4">
            {/* 제목 + 언어 */}
            <div className="d-flex justify-content-between align-items-start mb-1 gap-2">
              <h3 className="fw-bold mb-3">{post.title}</h3>

              {(post.usingLanguage || post.learningLanguage) && (
                <div className="language-badge">
                  {post.usingLanguage || "—"} → {post.learningLanguage || "—"}
                </div>
              )}
            </div>

            {/* 작성일 */}
            <div className="text-muted small mb-4">{createdText}</div>

            {/* 작성자 + 친구 신청 */}
            <div className="d-flex align-items-center mb-4 gap-3">
              <div
                className="d-flex align-items-center"
                style={{ cursor: "pointer" }}
                onClick={() => navigate(`/users/${post.authorId}/profile`)}
              >
                {" "}
                {/* 프로필 이동 */}
                <img
                  src={post.authorImageUrl || defaultProfile}
                  alt="author"
                  className="rounded-circle"
                  style={{ width: 44, height: 44, objectFit: "cover" }}
                />
                <div className="fw-semibold ms-1">{post.authorName}</div>
              </div>

              {renderMatchButton()}
            </div>

            {/* 이미지 */}
            {post.imageUrls?.length > 0 && (
              <div className="mb-4">
                {post.imageUrls.map((url, idx) => (
                  <img
                    key={idx}
                    src={url || defaultImage}
                    alt={`post-${idx}`}
                    className="img-fluid rounded mb-2"
                    style={{ maxHeight: 360, objectFit: "cover" }}
                  />
                ))}
              </div>
            )}

            {/* 본문 */}
            <p className="post-content">{post.content || "내용이 없습니다."}</p>

            {/* 메타 정보 */}
            <div className="d-flex justify-content-between align-items-center mt-4 text-muted small">
              <span>조회 {post.viewCount}</span>
              <div className="d-flex gap-3">
                <span style={{ cursor: "pointer" }} onClick={onToggleHeart}>
                  {hearted ? "❤️" : "🤍"} {heartCount}
                </span>
                <span>💬 {post.commentCount}</span>
              </div>
            </div>

            {/* 댓글 영역 */}
            {post && <CommentSection postId={post.id} />}
          </div>
        </div>
      </div>
    </InfoLayout>
  );
};

export default Post;
