import React from "react";

const Comment = ({ comment, onLike }) => {
  return (
    <div className="card mb-2">
      <div className="card-body">
        <strong>{comment.userNickname}</strong>
        <p>{comment.content}</p>
        <button
          className="btn btn-outline-danger btn-sm"
          onClick={() => onLike(comment.commentId, "COMMENT")}
        >
          ❤️ {comment.likeCount}
        </button>
      </div>
    </div>
  );
};

export default Comment;
