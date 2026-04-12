import { useEffect, useState } from "react";
import { getUserAddresses } from "../api/addressApi";
import { placeOrder } from "../api/orderApi";
import { useNavigate } from "react-router-dom";

function Checkout() {
  const [addresses, setAddresses] = useState([]);
  const [selectedAddressId, setSelectedAddressId] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchAddresses = async () => {
      try {
        const res = await getUserAddresses();

        console.log("Checkout Addresses:", res.data); // 🔍 DEBUG

        setAddresses(res.data || []);
      } catch (err) {
        console.error("Error fetching addresses", err);
      }
    };

    fetchAddresses();
  }, []);

  const handleOrder = async () => {
    if (!selectedAddressId) {
      alert("Please select an address");
      return;
    }

    try {
      const res = await placeOrder(selectedAddressId);

      navigate(`/payment?orderId=${res.data.orderId}`);
    } catch (err) {
      console.error(err);
      alert("Order failed");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Select Address</h2>

      {/* ✅ Empty state */}
      {addresses.length === 0 && (
        <p>No addresses found. Please add one from User page.</p>
      )}

      {addresses.map((addr) => (
        <div key={addr.addressId} style={{ margin: "10px 0" }}>
          <input
            type="radio"
            name="address"
            value={addr.addressId}
            onChange={() => setSelectedAddressId(addr.addressId)}
          />

          {addr.addressLine1}, {addr.city}, {addr.state}
        </div>
      ))}

      <br />

      <button onClick={handleOrder}>
        Confirm Order
      </button>
    </div>
  );
}

export default Checkout;