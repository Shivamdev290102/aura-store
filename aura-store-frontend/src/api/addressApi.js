import API from "./axios";
import useAuthStore from "../store/authStore";

// Get addresses
export const getUserAddresses = () => {
  const user = useAuthStore.getState().user;
  return API.get(`/address/user/${user.userId}`);
};

export const addAddress = (data) => {
  const user = useAuthStore.getState().user;

  return API.post(`/address/add/${user.userId}`, data);
};