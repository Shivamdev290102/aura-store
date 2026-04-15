import { useState } from "react";
import { registerUser } from "../api/userApi";
import useAuthStore from "../store/authStore";
import { useNavigate } from "react-router-dom";

function Login() {
  const [isRegister, setIsRegister] = useState(false);
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    phone: ""
  });

  const setUser = useAuthStore((s) => s.setUser);
  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      if (isRegister) {
        const res = await registerUser(form);

        if (!res.data?.userId) {
          alert("Registration failed");
          return;
        }

        alert("Registered successfully. Please login.");
        setIsRegister(false);
        return;
      }

      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: form.email,
          password: form.password,
        }),
      });

      if (!res.ok) {
        const text = await res.text();
        console.error("Login failed response:", text);
        alert("Login failed");
        return;
      }

      const data = await res.json();

      if (data.token) {
        setUser(data.user, data.token);
        navigate("/");
      } else {
        alert("Invalid credentials");
      }

    } catch (err) {
      console.error(err);
      alert("Login failed");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>{isRegister ? "Register" : "Login"}</h2>

      {isRegister && (
        <input placeholder="Name"
          onChange={(e) => setForm({ ...form, name: e.target.value })}
        />
      )}

      <input placeholder="Email"
        onChange={(e) => setForm({ ...form, email: e.target.value })}
      />

      <input type="password" placeholder="Password"
        onChange={(e) => setForm({ ...form, password: e.target.value })}
      />

      {isRegister && (
        <input placeholder="Phone"
          onChange={(e) => setForm({ ...form, phone: e.target.value })}
        />
      )}

      <br /><br />

      <button onClick={handleSubmit}>
        {isRegister ? "Register" : "Login"}
      </button>

      <p onClick={() => setIsRegister(!isRegister)} style={{ cursor: "pointer" }}>
        {isRegister ? "Login" : "Register"}
      </p>
    </div>
  );
}

export default Login;