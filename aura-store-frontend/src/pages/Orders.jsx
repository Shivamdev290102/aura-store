import { useEffect, useState } from "react";
import { getUserOrders, trackOrder } from "../api/orderApi";
import { useNavigate } from "react-router-dom";

function Orders() {
  const [orders, setOrders] = useState([]);
  const [trackingMap, setTrackingMap] = useState({});
  const [loadingMap, setLoadingMap] = useState({});

  const navigate = useNavigate();

  // Load orders only (no auto tracking)
  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await getUserOrders();
        setOrders(res.data || []);
      } catch (err) {
        console.error("Error fetching orders:", err);
      }
    };

    fetchOrders();
  }, []);

  // 🔥 Track order (manual click)
  const fetchTracking = async (orderId) => {
    try {
      setLoadingMap((prev) => ({ ...prev, [orderId]: true }));

      const res = await trackOrder(orderId);

      setTrackingMap((prev) => ({
        ...prev,
        [orderId]: res.data,
      }));
    } catch (err) {
      console.error("Tracking error:", err);
      alert("Tracking failed");
    } finally {
      setLoadingMap((prev) => ({ ...prev, [orderId]: false }));
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Your Orders</h2>

      {orders.length === 0 && <p>No orders found</p>}

      {orders.map((order) => {
        const tracking = trackingMap[order.orderId];

        // Normalize payment status
        const paymentStatus =
          tracking?.paymentStatus?.toUpperCase().trim() || "";

        return (
          <div
            key={order.orderId}
            style={{
              border: "1px solid #ddd",
              padding: "15px",
              margin: "15px 0",
              borderRadius: "10px",
              display: "flex",
              flexDirection: "column",
              gap: "8px",
            }}
          >
            {/* Header */}
            <strong>Order ID: {order.orderId}</strong>

            {/* ✅ FIX: Show latest status */}
            <p>
              <strong>Status:</strong>{" "}
              {tracking?.orderStatus || order.orderStatus}
            </p>

            <p><strong>Total:</strong> ₹{order.totalAmount}</p>

            {/* 🔥 Track Button */}
            <button
              onClick={() => fetchTracking(order.orderId)}
              disabled={loadingMap[order.orderId]}
            >
              {loadingMap[order.orderId] ? "Loading..." : "Track Order"}
            </button>

            {/* 🔥 Show tracking after click */}
            {tracking && (
              <div style={{ marginTop: "10px" }}>
                <p><strong>Order Status:</strong> {tracking.orderStatus}</p>
                <p><strong>Payment:</strong> {tracking.paymentStatus}</p>
                <p><strong>Shipment:</strong> {tracking.shipmentStatus}</p>
                <p><strong>Tracking #:</strong> {tracking.trackingNumber}</p>

                {/* 🔥 Retry only when needed */}
                {(paymentStatus === "FAILED" ||
                  paymentStatus.includes("INITIATED")) && (
                  <button
                    onClick={() =>
                      navigate(`/payment?orderId=${order.orderId}`)
                    }
                    style={{
                      marginTop: "10px",
                      backgroundColor: "#ff9800",
                      color: "white",
                      border: "none",
                      padding: "8px",
                      borderRadius: "5px",
                      cursor: "pointer",
                    }}
                  >
                    Retry Payment
                  </button>
                )}
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
}

export default Orders;