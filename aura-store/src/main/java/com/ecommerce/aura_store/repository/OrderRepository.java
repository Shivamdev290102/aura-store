package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
