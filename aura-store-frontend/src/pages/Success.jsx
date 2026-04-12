import { useLocation } from "react-router-dom";

function Success() {
  const query = new URLSearchParams(useLocation().search);
  const orderId = query.get("orderId");

  return (
    <div style={{ padding: "20px" }}>
      <h2>Order Placed Successfully 🎉</h2>

      <p>Your Order ID: {orderId}</p>
    </div>
  );
}

export default Success;