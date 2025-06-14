package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Double priceAtOrder;
}