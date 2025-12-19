import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 5000,
});

// 게시글 목록 조회 API
const API_BASE_URL = "http://localhost:8080";

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

// 🔥 필요하면 이런 식으로 점점 추가 가능:
// export const createPost = (data) => api.post("/posts", data);
// export const searchPosts = (params) => api.get("/posts/search", { params });

export default api;
