package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.Cart;
import com.ecommerce.aura_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserUserId(Long userUserId);


}
