// Product.java
package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer id;
    private String pName;
    private Double price;
    private Integer qty;
    private Boolean isDeleted;
    private String pUuid;
    private String category;
}