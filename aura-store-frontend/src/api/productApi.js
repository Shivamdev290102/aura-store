import API from "./axios";

export const getAllProducts = () => {
  return API.get("/products");
};

export const getProductById = (id) => {
  return API.get(`/products/${id}`);
};