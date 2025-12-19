import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const api = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  timeout: 5000,
});

// 게시글 목록 조회 API
export const fetchPosts = ({ sortType, cursorKey, cursorId, size }) => {
  return axios.get(`${API_BASE_URL}/api/posts`, {
    params: {
      sortType,
      cursorKey,
      cursorId,
      size,
    },
  });
};

//추후 이런 식으로 점점 추가 가능:
//export const createPost = (data) => api.post("/posts", data);
//export const searchPosts = (params) => api.get("/posts/search", { params });

export default api;
