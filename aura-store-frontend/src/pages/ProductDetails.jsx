import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProductById } from "../api/productApi";
import { addToCart } from "../api/cartApi";

function ProductDetails() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);

  useEffect(() => {
    if (!id) return;

    getProductById(id)
      .then((res) => {
        console.log("Product API:", res.data); // 👈 DEBUG
        setProduct(res.data);
      })
      .catch((err) => {
        console.error("Error loading product:", err);
      });
  }, [id]);

  const handleAddToCart = async () => {
    try {
      await addToCart(id, 1);
      alert("Added to cart");
    } catch (err) {
      console.error(err);
      alert("Failed to add to cart");
    }
  };

  // ✅ Prevent blank screen
  if (!product) return <h2>Loading product...</h2>;

  return (
    <div style={{ padding: "20px" }}>
      <img src={product.imageUrl} width="200" />

      <h2>{product.name}</h2>
      <p>{product.description}</p>
      <h3>₹{product.price}</h3>

      <button onClick={handleAddToCart}>
        Add to Cart
      </button>
    </div>
  );
}

export default ProductDetails;