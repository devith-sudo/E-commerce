package view;

// View order summary

import java.text.SimpleDateFormat;

public class OrderView {
    public void showOrderSummary(OrderSummaryDTO order) {
        System.out.println("=== ✅ Order Confirmation ===");
        System.out.printf("Order Code: %s\n", order.getOrderCode());
        System.out.printf("Order Date: %s\n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getOrderDate()));
        System.out.printf("Total Items: %d\n", order.getTotalItems());
        System.out.printf("Total Price: $%.2f\n", order.getTotalPrice());
        System.out.println("Products:");
        order.getProductSummaries().forEach(item -> {
            System.out.printf("- %s x%d @ $%.2f each\n",
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice());
        });
    }

    public void showOrderSuccessMessage() {
        System.out.println("🎉 Order placed successfully!");
    }
}
