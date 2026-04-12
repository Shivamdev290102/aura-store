import { useEffect, useState } from "react";
import { getUserAddresses, addAddress } from "../api/addressApi";

function User() {
  const [addresses, setAddresses] = useState([]);
  const [showForm, setShowForm] = useState(false);

  const [form, setForm] = useState({
    addressLine1: "",
    addressLine2: "",
    city: "",
    state: "",
    pinCode: ""
  });

  const fetchAddresses = async () => {
    try {
      const res = await getUserAddresses();

      console.log("Addresses:", res.data); // 🔍 DEBUG

      setAddresses(res.data || []);
    } catch (err) {
      console.error("Failed to load addresses", err);
    }
  };

  useEffect(() => {
    fetchAddresses();
  }, []);

  const handleAddAddress = async () => {
    try {
      await addAddress(form);

      alert("Address added");

      setShowForm(false);
      setForm({
        addressLine1: "",
        addressLine2: "",
        city: "",
        state: "",
        pinCode: ""
      });

      fetchAddresses(); // 🔥 refresh list
    } catch (err) {
      console.error(err);
      alert("Failed to add address");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>Your Addresses</h2>

      {/* ✅ Handle empty list */}
      {addresses.length === 0 && <p>No addresses found</p>}

      {addresses.map((addr) => (
        <div
          key={addr.addressId}
          style={{
            border: "1px solid #ccc",
            margin: "10px 0",
            padding: "10px"
          }}
        >
          <p>{addr.addressLine1}</p>
          <p>{addr.addressLine2}</p>
          <p>{addr.city}, {addr.state}, {addr.pinCode}</p>  
        </div>
      ))}

      <button onClick={() => setShowForm(!showForm)}>
        Add Address
      </button>

      {showForm && (
        <div style={{ marginTop: "20px" }}>
          <input
            placeholder="Address Line 1"
            value={form.addressLine1}
            onChange={(e) =>
              setForm({ ...form, addressLine1: e.target.value })
            }
          />

          <input
            placeholder="Address Line 2"
            value={form.addressLine2}
            onChange={(e) =>
              setForm({ ...form, addressLine2: e.target.value })
            }
          />

          <input
            placeholder="City"
            value={form.city}
            onChange={(e) =>
              setForm({ ...form, city: e.target.value })
            }
          />

          <input
            placeholder="State"
            value={form.state}
            onChange={(e) =>
              setForm({ ...form, state: e.target.value })
            }
          />

          <input
            placeholder="Pincode"
            value={form.pinCode}
            onChange={(e) =>
              setForm({ ...form, pinCode: e.target.value })
            }
          />

          <br /><br />
          <button onClick={handleAddAddress}>Save</button>
        </div>
      )}
    </div>
  );
}

export default User;