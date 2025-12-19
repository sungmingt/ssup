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
};
