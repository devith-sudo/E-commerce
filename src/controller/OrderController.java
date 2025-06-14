package controller;

import model.Order;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderView orderView;

    public OrderController(OrderService orderService, ProductService productService, OrderView orderView) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderView = orderView;
    }

    public void viewOrderHistory() {
        if (!SessionService.isLoggedIn()) {
            orderView.displayError("You must be logged in to view orders");
            return;
        }

        try {
            List<Order> orders = orderService.getOrdersByUserId(SessionService.getCurrentUser().getUserId());
            orderView.displayOrders(orders);
        } catch (SQLException e) {
            orderView.displayError("Error fetching orders: " + e.getMessage());
        }
    }

    public void viewOrderDetails(UUID orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                orderView.displayError("Order not found");
                return;
            }

            List<OrderItem> items = orderService.getOrderItems(orderId);
            List<Product> products = new ArrayList<>();
            for (OrderItem item : items) {
                products.add(productService.getProductById(item.getProductId()));
            }

            orderView.displayOrderDetails(order, items, products);
        } catch (SQLException e) {
            orderView.displayError("Error fetching order details: " + e.getMessage());
        }
    }

    public void checkout(OrderDTO orderDTO) {
        try {
            Order order = orderService.createOrder(orderDTO);
            orderView.displayOrderCreationSuccess(order);
        } catch (SQLException e) {
            orderView.displayError("Error creating order: " + e.getMessage());
        }
    }
}