import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import CommentItem from "./CommentItem.jsx";
import CommentForm from "./CommentForm.jsx";

const CommentList = ({ comments, onRefresh, postId }) => {
  const [editingId, setEditingId] = useState(null);

  return (
    <>
      {comments.map((comment) =>
        editingId === comment.id ? (
          <CommentForm
            key={comment.id}
            mode="edit"
            postId={postId}
            comment={comment}
            onCancel={() => setEditingId(null)}
            onSuccess={() => {
              setEditingId(null);
              onRefresh();
            }}
          />
        ) : (
          <CommentItem
            key={comment.id}
            comment={comment}
            onRefresh={onRefresh}
            authorId={comment.authorId}
            onEdit={() => setEditingId(comment.id)}
          />
        )
      )}
    </>
  );
};

export default CommentList;
