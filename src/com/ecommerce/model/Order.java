package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer id;
    private Integer userId;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderProduct> orderProducts; // Add this field

    // Add getter and setter if not using Lombok
    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}