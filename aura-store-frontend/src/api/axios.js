import axios from "axios";
import useAuthStore from "../store/authStore";

const API = axios.create({
  baseURL: "http://localhost:8080/api",
});

API.interceptors.request.use((config) => {
  const token = useAuthStore.getState().token;

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

API.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 403) {
      alert("Session expired. Login again.");
      useAuthStore.getState().logout();
    }
    return Promise.reject(err);
  }
);

export default API;