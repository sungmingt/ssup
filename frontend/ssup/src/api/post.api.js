import { api } from "./axios";

export const postApi = {
  getPostList: ({
    sortType,
    cursorKey,
    cursorId,
    size,
    locationId,
    usingLanguage,
    learningLanguage,
    interestId,
  }) =>
    api.get("/posts", {
      params: {
        sortType,
        cursorKey,
        cursorId,
        size,
        locationId,
        usingLanguage,
        learningLanguage,
        interestId,
      },
    }),

  getPost: (postId) => api.get(`/posts/${postId}`),

  createPost: (formData) => api.post("/posts", formData),

  updatePost: (postId, formData) => api.put(`/posts/${postId}`, formData),

  deletePost: (postId) => api.delete(`/posts/${postId}`),

  toggleHeart: (postId) => api.post(`/posts/${postId}/hearts`),

  getFilterMedadata: () => api.get("/posts/filters"),
};
