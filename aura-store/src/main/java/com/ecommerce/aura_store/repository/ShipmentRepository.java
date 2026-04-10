package com.ecommerce.aura_store.repository;

import com.ecommerce.aura_store.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
