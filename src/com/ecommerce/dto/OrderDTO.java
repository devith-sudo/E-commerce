// OrderDTO.java
package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer userId;
    private List<OrderItemDTO> items;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class OrderItemDTO {
    private Integer productId;
    private Integer quantity;
}