import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { postApi } from "@/api";
import "bootstrap/dist/css/bootstrap.min.css";
import CommentList from "./CommentList.jsx";
import CommentForm from "./CommentForm.jsx";

const CommentSection = ({ postId }) => {
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchComments = async () => {
    try {
      const res = await postApi.getCommentList(postId);
      setComments(res.data);
    } catch (e) {
      console.error("댓글 조회 실패", e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchComments();
  }, [postId]);

  if (loading) return <div className="text-muted">댓글 불러오는 중...</div>;

  return (
    <>
      <div className="mt-5 mb-4">
        <h6 className="fw-bold mb-3">댓글 {comments.length}</h6>

        <CommentForm postId={postId} onSuccess={fetchComments} />
      </div>

      <CommentList
        postId={postId}
        comments={comments}
        onRefresh={fetchComments}
      />
    </>
  );
};

export default CommentSection;
