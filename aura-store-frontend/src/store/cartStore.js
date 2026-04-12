import { create } from "zustand";
import { getCart } from "../api/cartApi";

const useCartStore = create((set) => ({
  cart: null,

  fetchCart: async () => {
    const res = await getCart(1); // temporary userId
    set({ cart: res.data });
  }
}));

export default useCartStore;