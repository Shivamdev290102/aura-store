package com.ecommerce.aura_store.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String description;
    private Integer stockQuantity;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sku;
}
