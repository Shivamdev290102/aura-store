import { useEffect } from "react";
import useCartStore from "../store/cartStore";
import { useNavigate } from "react-router-dom";

function Cart() {
  const { cart, fetchCart } = useCartStore();
  const navigate = useNavigate();

  useEffect(() => {
    fetchCart();
  }, []);

  if (!cart) return <h2>Loading...</h2>;

  // 🧮 Calculate total
  const totalAmount = cart.items.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  return (
    <div style={{ padding: "20px" }}>
      <h2>Your Cart</h2>

      {cart.items.length === 0 && <p>Cart is empty</p>}

      {cart.items.map((item) => (
        <div key={item.productId} style={{ marginBottom: "10px" }}>
          <p>{item.productName}</p>
          <p>Qty: {item.quantity}</p>
          <p>₹{item.price}</p>
        </div>
      ))}

      <hr />

      <h3>Total: ₹{totalAmount.toFixed(2)}</h3>

      {/* 🔥 Place Order Button */}
      {cart.items.length > 0 && (
        <button
          onClick={() => navigate("/checkout")}
          style={{ marginTop: "20px" }}
        >
          Place Order
        </button>
      )}
    </div>
  );
}

export default Cart;