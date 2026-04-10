package com.ecommerce.aura_store.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
