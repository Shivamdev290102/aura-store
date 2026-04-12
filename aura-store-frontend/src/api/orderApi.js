import API from "./axios";
import useAuthStore from "../store/authStore";

// Place order
export const placeOrder = (addressId) => {
  const user = useAuthStore.getState().user;

  return API.post("/orders/place", {
    userId: user.userId,
    addressId
  });
};

// Get all orders for logged in user
export const getUserOrders = () => {
  const user = useAuthStore.getState().user;
  return API.get(`/orders/user/${user.userId}`);
};

// Get order by ID
export const getOrderById = (orderId) => {
  return API.get(`/orders/${orderId}`);
};

// Track order
export const trackOrder = (orderId) => {
  return API.get(`/orders/${orderId}/track`);
};