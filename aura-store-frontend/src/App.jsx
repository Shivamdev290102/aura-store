import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Home from "./pages/Home";
import Login from "./pages/Login";
import ProductDetails from "./pages/ProductDetails";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import Success from "./pages/Success";
import User from "./pages/User";
import Orders from "./pages/Orders";
import Payment from "./pages/Payment";

import Navbar from "./components/Navbar";
import useAuthStore from "./store/authStore";

function App() {
  const user = useAuthStore((s) => s.user);

  return (
    <BrowserRouter>
      {/* Show Navbar only after login */}
      {user && <Navbar />}

      <Routes>
        {/* Public Route */}
        <Route path="/login" element={<Login />} />

        {/* Protected Routes */}
        <Route
          path="/"
          element={user ? <Home /> : <Navigate to="/login" />}
        />

        <Route
          path="/product/:id"
          element={user ? <ProductDetails /> : <Navigate to="/login" />}
        />

        <Route
          path="/cart"
          element={user ? <Cart /> : <Navigate to="/login" />}
        />

        <Route
          path="/checkout"
          element={user ? <Checkout /> : <Navigate to="/login" />}
        />

        <Route
          path="/orders"
          element={user ? <Orders /> : <Navigate to="/login" />}
        />

        <Route
          path="/success"
          element={user ? <Success /> : <Navigate to="/login" />}
        />

        <Route
          path="/user"
          element={user ? <User /> : <Navigate to="/login" />}
        />

        <Route
          path="/payment"
          element={user ? <Payment /> : <Navigate to="/login" />}
        />

        {/* Fallback route */}
        <Route
          path="*"
          element={<Navigate to={user ? "/" : "/login"} />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;