package com.ecommerce.aura_store.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;

}
