import { useState } from "react";
import { registerUser, getUserByEmail } from "../api/userApi";
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
      let res;

      if (isRegister) {
        res = await registerUser(form);
      } else {
        res = await getUserByEmail(form.email);
      }

      setUser(res.data);
      navigate("/");
    } catch (err) {
      alert("Login failed or user not found");
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2>{isRegister ? "Register" : "Login"}</h2>

      {isRegister && (
        <input
          placeholder="Name"
          onChange={(e) =>
            setForm({ ...form, name: e.target.value })
          }
        />
      )}

      <input
        placeholder="Email"
        onChange={(e) =>
          setForm({ ...form, email: e.target.value })
        }
      />

      <input
        type="password"
        placeholder="Password"
        onChange={(e) =>
          setForm({ ...form, password: e.target.value })
        }
      />

      {isRegister && (
        <input
          placeholder="Phone"
          onChange={(e) =>
            setForm({ ...form, phone: e.target.value })
          }
        />
      )}

      <br /><br />

      <button onClick={handleSubmit}>
        {isRegister ? "Register" : "Login"}
      </button>

      <p
        style={{ cursor: "pointer", marginTop: "10px" }}
        onClick={() => setIsRegister(!isRegister)}
      >
        {isRegister
          ? "Already have account? Login"
          : "New user? Register"}
      </p>
    </div>
  );
}

export default Login;