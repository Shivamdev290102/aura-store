package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartItemId(Long cartItemId);

    CartItem deleteCartItemByCartItemId(Long cartItemId);
}
