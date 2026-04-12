import API from "./axios";

export const processPayment = (orderId, paymentMethod) => {
  return API.post("/payments/process", {
    orderId,
    paymentMethod
  });
};