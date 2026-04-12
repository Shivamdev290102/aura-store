import API from "./axios";
import useAuthStore from "../store/authStore";

// Get cart
export const getCart = () => {
  const user = useAuthStore.getState().user;
  return API.get(`/cart/${user.userId}`);
};

// Add to cart
export const addToCart = (productId, quantity = 1) => {
  const user = useAuthStore.getState().user;

  return API.post("/cart/add", {
    userId: user.userId,
    productId,
    quantity
  });
};

// Update cart item
export const updateCartItem = (cartItemId, productId, quantity) => {
  const user = useAuthStore.getState().user;

  return API.put(`/cart/item/${cartItemId}`, {
    userId: user.userId,
    productId,
    quantity
  });
};

// Delete cart item
export const deleteCartItem = (cartItemId) => {
  return API.delete(`/cart/item/${cartItemId}`);
};