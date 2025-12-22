import { api } from "./axios";

export const postApi = {
  getPostList: ({ sortType, cursorKey, cursorId, size }) =>
    api.get("/posts", {
      params: {
        sortType,
        cursorKey,
        cursorId,
        size,
      },
    }),

  getPost: (postId) => api.get(`/posts/${postId}`),

  createPost: (formData) => api.post("/posts", formData),

  updatePost: (postId, formData) => api.put(`/posts/${postId}`, formData),

  deletePost: (postId) => api.delete(`/posts/${postId}`),

  toggleHeart: (postId) => api.post(`/posts/${postId}/hearts`),

  //comment
  getCommentList: (postId) => api.get(`/posts/${postId}/comments`),

  createComment: (postId, formData) =>
    api.post(`/posts/${postId}/comments`, formData),

  updateComment: (postId, commentId, formData) =>
    api.put(`/posts/${postId}/comments/${commentId}`, formData),

  deleteComment: (postId, commentId) =>
    api.delete(`/posts/${postId}/comments/${commentId}`),
};
