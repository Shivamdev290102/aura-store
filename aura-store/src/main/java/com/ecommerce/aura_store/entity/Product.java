package com.ecommerce.aura_store.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;
    private String category;
    private String brand;
    private String imageUrl;

    @Column(nullable = false)
    private Integer stockQuantity;
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(unique = true, nullable = false)
    private String sku;
}
