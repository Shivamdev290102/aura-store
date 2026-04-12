import { useNavigate } from "react-router-dom";

function ProductCard({ product }) {
  const navigate = useNavigate();

  return (
    <div
      onClick={() => navigate(`/product/${product.productId}`)}
      style={{ border: "1px solid #ccc", padding: "10px", cursor: "pointer" }}
    >
      <img src={product.imageUrl} width="150" />
      <h3>{product.name}</h3>
      <p>₹{product.price}</p>
    </div>
  );
}

export default ProductCard;