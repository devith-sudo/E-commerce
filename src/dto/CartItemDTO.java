package dto;

import lombok.Getter;

@Getter
public class CartItemDTO {
    // Getters
    private String productName;
    private int quantity;
    private double unitPrice;

    // Constructor
    public CartItemDTO(String productName, int quantity, double unitPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return quantity * unitPrice;
    }

    // Optional: toString() for debugging
    @Override
    public String toString() {
        return "productName='" + productName + '\'' + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", totalPrice=" + getTotalPrice();
    }
}

