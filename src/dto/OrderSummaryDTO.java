package dto;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class OrderSummaryDTO {
    private String orderCode;
    private Date orderDate;
    private int totalItems;
    private double totalPrice;
    private List<CartItemDTO> productSummaries;

    public OrderSummaryDTO(String orderCode, Date orderDate, int totalItems, double totalPrice, List<CartItemDTO> productSummaries) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
        this.productSummaries = productSummaries;
    }

}

