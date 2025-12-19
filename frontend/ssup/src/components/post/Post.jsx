import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import "./../../css/Post.css";
import defaultProfile from "../../assets/ssup_user_default_image.png";
import defaultImage from "./../../assets/ssup_post_default_image.webp";

const API_BASE_URL = "http://localhost:8080";

const Post = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);

  const isMyPost = true; //TODO: 로그인 유저 id === post.authorId

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const res = await axios.get(`${API_BASE_URL}/api/posts/${id}`);
        setPost(res.data);
      } catch (err) {
        console.error("게시글 상세 조회 실패", err);
      } finally {
        setLoading(false);
      }
    };

    fetchPost();
  }, [id]);

  const onMatchRequest = async () => {
    if (!post) return;

    try {
      await axios.post(`${API_BASE_URL}/api/matchRequest`, {
        postId: post.id,
        targetUserId: post.authorId, // 추후 내려오게 되면 교체
        requesterId: 1, // 로그인 유저
      });
      alert("친구 신청을 보냈습니다.");
    } catch {
      alert("친구 신청에 실패했습니다.");
    }
  };

  const onDelete = async () => {
    const ok = window.confirm("정말 이 게시글을 삭제하시겠습니까?");
    if (!ok) return;

    try {
      await axios.delete(`http://localhost:8080/api/posts/${post.id}`, {
        withCredentials: true,
      });

      alert("게시글이 삭제되었습니다.");
      navigate("/posts");
    } catch (e) {
      console.error(e);
      alert("게시글 삭제에 실패했습니다.");
    }
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
    : createdAt.toLocaleDateString();

  return (
    <div className="post-page-wrapper py-5">
      <div className="post-detail-wrapper mx-auto">
        {/* 뒤로가기 */}
        <button
          className="btn btn-sm btn-outline-secondary mb-4"
          onClick={() => navigate("/posts")}
        >
          ← 목록으로
        </button>

        {/* 메인 카드 */}
        <div className="post-main-card bg-white shadow-sm rounded p-4">
          {/* 제목 + 수정/삭제 버튼 */}
          <div className="d-flex justify-content-between align-items-start mb-2">
            <h3 className="fw-bold mb-0">{post.title}</h3>

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

          {/* 작성일 */}
          <div className="text-muted small mb-4">{createdText}</div>

          {/* 작성자 */}
          <div className="d-flex align-items-center mb-2">
            <img
              src={post.authorImageUrl || defaultProfile}
              alt="author"
              className="rounded-circle"
              style={{ width: 44, height: 44, objectFit: "cover" }}
            />
            <div className="fw-semibold">{post.authorName}</div>
          </div>

          {/* 언어 + 친구신청 버튼 */}
          <div className="d-flex justify-content-between align-items-center mb-4">
            <div className="text-muted small">
              {post.usingLanguage && post.learningLanguage && (
                <>
                  {post.usingLanguage} → {post.learningLanguage}
                </>
              )}
            </div>

            <button
              className="btn btn-sm"
              style={{ backgroundColor: "#b9e3b7a5" }}
              onClick={onMatchRequest}
            >
              친구 신청
            </button>
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
              <span>❤️ {post.heartCount}</span>
              <span>💬 {post.commentCount}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Post;

// import { useEffect, useState } from "react";
// import { useParams, useNavigate } from "react-router-dom";
// import axios from "axios";
// import "bootstrap/dist/css/bootstrap.min.css";
// import "./../../css/Post.css";

// const API_BASE_URL = "http://localhost:8080";

// const Post = () => {
//   const { id } = useParams();
//   const navigate = useNavigate();

//   const [post, setPost] = useState(null);
//   const [loading, setLoading] = useState(true);

//   const defaultImage = "./../../assets/ssup_post_default_image.jpg";
//   const defaultProfile = "./../../assets/ssup_user_default_image.png";

//   useEffect(() => {
//     const fetchPost = async () => {
//       try {
//         const res = await axios.get(`${API_BASE_URL}/api/posts/${id}`);
//         setPost(res.data);
//       } catch (err) {
//         console.error("상세 조회 실패:", err);
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchPost();
//   }, [id]);

//   /** 친구 신청 */
//   const onMatchRequest = async () => {
//     if (!post) return;

//     try {
//       const res = await fetch(`${API_BASE_URL}/api/matchRequest`, {
//         method: "POST",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify({
//           postId: post.id,
//           targetUserId: post.authorId, // 추후 API에서 내려주도록 확장
//           requesterId: 1, // 로그인 유저 ID (상태/토큰으로 교체 예정)
//         }),
//       });

//       if (!res.ok) throw new Error();
//       alert("친구 신청을 보냈습니다!");
//     } catch {
//       alert("친구 신청 실패!");
//     }
//   };

//   if (loading) {
//     return <p className="text-center mt-5">불러오는 중...</p>;
//   }

//   if (!post) {
//     return <p className="text-center mt-5">게시글을 찾을 수 없습니다.</p>;
//   }

//   const createdAt = new Date(post.createdAt);
//   const createdText = isNaN(createdAt)
//     ? "날짜 없음"
//     : createdAt.toLocaleDateString();

//   return (
//     <div className="post-page-wrapper py-5">
//       <div className="post-detail-wrapper mx-auto">
//         {/* 뒤로가기 */}
//         <button
//           className="btn btn-outline-secondary mb-4"
//           onClick={() => navigate("/posts")}
//         >
//           ← 글 목록으로
//         </button>

//         {/* 메인 카드 */}
//         <div className="border rounded p-4 shadow-sm bg-white post-main-card">
//           {/* 제목 */}
//           <h2 className="title mb-3">{post.title || "제목 없음"}</h2>

//           {/* 언어 + CTA */}
//           <div className="d-flex justify-content-between align-items-center mb-3">
//             <div className="text-muted">
//               {post.usingLanguage && post.learningLanguage && (
//                 <>
//                   🗣️ <strong>{post.usingLanguage}</strong> →{" "}
//                   <strong>{post.learningLanguage}</strong>
//                 </>
//               )}
//             </div>

//             <button
//               className="btn btn-sm"
//               style={{ backgroundColor: "#b9e3b7a5" }}
//               onClick={onMatchRequest}
//             >
//               친구 신청
//             </button>
//           </div>

//           {/* 작성자 */}
//           <div className="d-flex align-items-center mb-4">
//             <img
//               src={post.authorImageUrl || defaultProfile}
//               alt="author"
//               className="rounded-circle"
//               style={{
//                 width: "48px",
//                 height: "48px",
//                 objectFit: "cover",
//               }}
//             />
//             <div className="ms-3">
//               <div className="fw-semibold">{post.authorName}</div>
//             </div>
//           </div>

//           {/* 이미지 */}
//           {post.imageUrl && (
//             <img
//               src={post.imageUrl || defaultImage}
//               alt="post"
//               className="img-fluid rounded mb-4"
//               style={{ maxHeight: "360px", objectFit: "cover" }}
//             />
//           )}

//           {/* 본문 */}
//           <p className="fs-5" style={{ whiteSpace: "pre-line" }}>
//             {post.content || "내용이 없습니다."}
//           </p>

//           {/* 메타 정보 */}
//           <div className="d-flex justify-content-between align-items-center mt-4 text-muted">
//             <small>
//               {createdText} · 조회 {post.viewCount}
//             </small>

//             <div className="d-flex gap-3 fs-5">
//               <span>❤️ {post.heartCount}</span>
//               <span>💬 {post.commentCount}</span>
//             </div>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default Post;
