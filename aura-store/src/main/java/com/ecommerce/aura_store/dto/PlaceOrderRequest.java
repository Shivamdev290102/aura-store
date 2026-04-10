package com.ecommerce.aura_store.dto;

import lombok.Data;

@Data
public class PlaceOrderRequest {
    private Long userId;
    private Long addressId;
}
