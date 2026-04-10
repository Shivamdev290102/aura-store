package com.ecommerce.aura_store.entity;

import com.ecommerce.aura_store.entity.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    private String trackingNumber;
    private LocalDate shippedDate;
    private LocalDate estimatedDeliveryDate;
    private LocalDate deliveredDate;
    private String deliveryAgent;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
