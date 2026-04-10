package com.ecommerce.aura_store.dto;

import com.ecommerce.aura_store.entity.OrderItem;
import com.ecommerce.aura_store.entity.enums.OrderStatus;
import com.ecommerce.aura_store.entity.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItem> orderItems;
    private String userName;
    private String address;

}
