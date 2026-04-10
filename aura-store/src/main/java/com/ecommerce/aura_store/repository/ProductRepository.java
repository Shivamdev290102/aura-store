package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductByProductId(Long productId);

}
