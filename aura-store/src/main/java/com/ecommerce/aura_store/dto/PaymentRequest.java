package com.ecommerce.aura_store.dto;

import com.ecommerce.aura_store.entity.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private PaymentMethod paymentMethod;
}
