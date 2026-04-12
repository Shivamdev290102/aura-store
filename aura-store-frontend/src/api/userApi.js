import API from "./axios";

// Register new user
export const registerUser = (data) => {
  return API.post("/user", data);
};

// Login (fetch by email - TEMP approach)
export const getUserByEmail = (email) => {
  return API.get(`/user/email/${email}`);
};