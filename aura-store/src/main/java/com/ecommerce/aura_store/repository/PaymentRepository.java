package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
