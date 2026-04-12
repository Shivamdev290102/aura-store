import { useLocation, useNavigate } from "react-router-dom";
import { useState } from "react";
import { processPayment } from "../api/paymentApi";

function Payment() {
  const location = useLocation();
  const navigate = useNavigate();

  const query = new URLSearchParams(location.search);
  const orderId = query.get("orderId");

  const [method, setMethod] = useState("UPI");
  const [loading, setLoading] = useState(false);

  const handlePayment = async () => {
    try {
      setLoading(true);

      const res = await processPayment(orderId, method);

      console.log("Payment response:", res.data);
      if (!orderId) {
      return <h2>Invalid Order</h2>;
    }
      navigate(`/success?orderId=${orderId}`);
    } catch (err) {
      console.error(err);
      alert("Payment failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Payment</h2>

      <p>Order ID: {orderId}</p>

      <h3>Select Payment Method</h3>

      <div>
        <input
          type="radio"
          value="UPI"
          checked={method === "UPI"}
          onChange={(e) => setMethod(e.target.value)}
        />
        UPI
      </div>

      <div>
        <input
          type="radio"
          value="Netbanking"
          checked={method === "Netbanking"}
          onChange={(e) => setMethod(e.target.value)}
        />
        Netbanking
      </div>

      <div>
        <input
          type="radio"
          value="Card"
          checked={method === "Card"}
          onChange={(e) => setMethod(e.target.value)}
        />
        Card
      </div>

      <div>
        <input
          type="radio"
          value="COD"
          checked={method === "COD"}
          onChange={(e) => setMethod(e.target.value)}
        />
        Cash on Delivery
      </div>

      <br />

      <button onClick={handlePayment} disabled={loading}>
        {loading ? "Processing..." : "Pay Now"}
      </button>
    </div>
  );
}

export default Payment;