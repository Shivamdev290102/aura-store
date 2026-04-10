package com.ecommerce.aura_store.dto;

import com.ecommerce.aura_store.entity.enums.OrderStatus;
import com.ecommerce.aura_store.entity.enums.PaymentStatus;
import com.ecommerce.aura_store.entity.enums.ShipmentStatus;
import lombok.Data;

@Data
public class TrackOrder {
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private ShipmentStatus shipmentStatus;
    private String trackingNumber;
}
