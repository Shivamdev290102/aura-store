import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

// Optional: add interceptor later for auth (JWT)
API.interceptors.response.use(
  (res) => res,
  (err) => {
    console.error("API Error:", err);
    return Promise.reject(err);
  }
);

export default API;