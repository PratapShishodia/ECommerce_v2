package com.ps.orderservice.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_order")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    private String orderId;
    private Long userId;
    private BigDecimal amount;
    private String orderStatus;
    private String paymentStatus;
    private Long paymentId;
    private LocalDateTime orderDate;
    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList;
}
