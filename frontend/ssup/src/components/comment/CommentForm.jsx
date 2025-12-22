import { useEffect, useState } from "react";
import { postApi } from "@/api";
import "bootstrap/dist/css/bootstrap.min.css";
import "./../../css/comment/CommentForm.css";

const CommentForm = ({
  mode = "create", //create/edit
  postId,
  comment, //edit일 때만 전달
  onSuccess,
  onCancel, //edit일 때만 전달
}) => {
  const isEdit = mode === "edit";

  const [content, setContent] = useState(isEdit ? comment.content : "");
  const [image, setImage] = useState(null); // 새 이미지
  const [imageUrl, setImageUrl] = useState(isEdit ? comment.imageUrl : null); // 기존 이미지
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    return () => {
      if (image) URL.revokeObjectURL(image);
    };
  }, [image]);

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim()) return alert("댓글을 입력해주세요.");

    const formData = new FormData();

    const dto = {
      content,
      ...(isEdit && { removeImage: imageUrl === null }),
    };

    formData.append(
      "dto",
      new Blob([JSON.stringify(dto)], {
        type: "application/json",
      })
    );

    if (image) {
      formData.append("image", image);
    }

    setSubmitting(true);

    try {
      if (isEdit) {
        await postApi.updateComment(postId, comment.id, formData);
      } else {
        await postApi.createComment(postId, formData);
        setContent("");
        setImage(null);
        setImageUrl(null);
      }
      onSuccess();
    } catch {
      alert(isEdit ? "댓글 수정에 실패했습니다." : "댓글 작성에 실패했습니다.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
      <form onSubmit={onSubmit}>
        <div className="comment-input-wrapper">
          <textarea
            className="form-control comment-textarea"
            rows={3}
            placeholder="댓글을 입력하세요"
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />

          {/* 카메라 버튼 */}
          <label className="comment-icon left">
            📷
            <input
              type="file"
              accept="image/*"
              hidden
              onChange={(e) => {
                const file = e.target.files[0];
                if (!file) return;
                setImage(file);
                setImageUrl(URL.createObjectURL(file));
              }}
            />
          </label>

          {/* 등록 / 수정 버튼 */}
          <button
            className="comment-submit-btn"
            type="submit"
            disabled={submitting}
          >
            {isEdit ? "수정" : "등록"}
          </button>

          {/* 수정 모드일 때만 취소 */}
          {isEdit && (
            <button
              type="button"
              className="comment-cancel-btn"
              onClick={onCancel}
            >
              취소
            </button>
          )}
        </div>
      </form>

      {(image || imageUrl) && (
        <div className="comment-image-preview">
          <img
            src={image ? URL.createObjectURL(image) : imageUrl}
            alt="preview"
          />
          <button
            type="button"
            className="remove-image-btn"
            onClick={() => {
              setImage(null);
              setImageUrl(null); //removeImage = true
            }}
          >
            ✕
          </button>
        </div>
      )}
    </>
  );
};

export default CommentForm;
