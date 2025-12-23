import { api } from "./axios";

export const commentApi = {
  getCommentList: (postId) => api.get(`/posts/${postId}/comments`),

  createComment: (postId, formData) =>
    api.post(`/posts/${postId}/comments`, formData),

  updateComment: (postId, commentId, formData) =>
    api.put(`/posts/${postId}/comments/${commentId}`, formData),

  deleteComment: (postId, commentId) =>
    api.delete(`/posts/${postId}/comments/${commentId}`),

  //heart
  toggleHeart: (commentId) => api.post(`/comments/${commentId}/hearts`),
};
