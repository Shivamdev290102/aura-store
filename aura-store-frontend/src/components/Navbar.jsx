import { Link } from "react-router-dom";

function Navbar() {
  return (
    <nav style={{ display: "flex", gap: "20px", padding: "10px" }}>
      <Link to="/">Home</Link>
      <Link to="/cart">Cart</Link>
      <Link to="/user">User</Link>
      <Link to="/orders">Orders</Link>
    </nav>
  );
}

export default Navbar;