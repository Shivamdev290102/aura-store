package com.ecommerce.aura_store.entity;

import com.ecommerce.aura_store.entity.enums.PaymentMethod;
import com.ecommerce.aura_store.entity.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private BigDecimal amount;

    private LocalDateTime paymentDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String transactionId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

}
