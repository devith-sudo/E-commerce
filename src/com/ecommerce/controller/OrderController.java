// OrderController.java
package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderProduct;
import com.ecommerce.model.Product;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.util.ConsoleUI;
import com.ecommerce.util.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {
    private final OrderService orderService;
    private final ProductService productService;
    private final AuthService authService;

    public OrderController(OrderService orderService, ProductService productService, AuthService authService) {
        this.orderService = orderService;
        this.productService = productService;
        this.authService = authService;
    }

    public void showOrderMenu() {
        String[] options;
        if (authService.isAdmin() || authService.isStaff()) {
            options = new String[]{"View My Orders", "View All Orders", "Create Order", "Back"};
        } else {
            options = new String[]{"View My Orders", "Create Order", "Back"};
        }

        ConsoleUI.displayMenu("Order Management", options);

        int choice = InputValidator.getValidInt("Enter your choice: ");
        switch (choice) {
            case 1:
                viewUserOrders();
                break;
            case 2:
                if (authService.isAdmin() || authService.isStaff()) {
                    viewAllOrders();
                } else {
                    createOrder();
                }
                break;
            case 3:
                if (authService.isAdmin() || authService.isStaff()) {
                    createOrder();
                } else {
                    return;
                }
                break;
            case 4:
                if (authService.isAdmin() || authService.isStaff()) {
                    return;
                } else {
                    ConsoleUI.displayMessage("Invalid choice. Please try again.");
                }
                break;
            default:
                ConsoleUI.displayMessage("Invalid choice. Please try again.");
        }
        showOrderMenu();
    }

    private void viewUserOrders() {
        int userId = authService.getCurrentUser().getId();
        List<Order> orders = orderService.getOrdersByUser(userId);
        displayOrders(orders);
    }

    private void viewAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        displayOrders(orders);
    }

    private void displayOrders(List<Order> orders) {
        List<Map<String, Object>> tableData = new ArrayList<>();

        for (Order order : orders) {
            Map<String, Object> row = new HashMap<>();
            row.put("Order ID", order.getId());
            row.put("User ID", order.getUserId());
            row.put("Total Amount", order.getTotalAmount());
            row.put("Status", order.getStatus());
            tableData.add(row);
        }

        ConsoleUI.displayTable(new String[]{"Order ID", "User ID", "Total Amount", "Status"}, tableData);
    }

    private void createOrder() {
        List<OrderProduct> orderItems = new ArrayList<>();
        boolean addingItems = true;

        while (addingItems) {
            // Show available products
            ProductController productController = new ProductController(productService, authService);
            productController.viewProducts();

            int productId = InputValidator.getValidInt("Enter product ID to add to order (0 to finish): ");
            if (productId == 0) {
                addingItems = false;
                continue;
            }

            Product product = productService.getProductById(productId);
            if (product == null) {
                ConsoleUI.displayMessage("Product not found.");
                continue;
            }

            int quantity = InputValidator.getValidInt(String.format("Enter quantity (max %d): ", product.getQty()));
            if (quantity <= 0 || quantity > product.getQty()) {
                ConsoleUI.displayMessage("Invalid quantity.");
                continue;
            }

            OrderProduct orderItem = new OrderProduct();
            orderItem.setProductId(productId);
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtOrder(product.getPrice());
            orderItems.add(orderItem);

            ConsoleUI.displayMessage("Product added to order.");
        }

        if (orderItems.isEmpty()) {
            ConsoleUI.displayMessage("No items in order. Cancelling.");
            return;
        }

        Order order = new Order();
        order.setUserId(authService.getCurrentUser().getId());
        order.setOrderProducts(orderItems);

        // Calculate total amount
        double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPriceAtOrder() * item.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);

        try {
            order = orderService.createOrder(order);
            ConsoleUI.displayMessage(String.format("Order created successfully! Order ID: %d, Total: %.2f",
                    order.getId(), order.getTotalAmount()));
        } catch (Exception e) {
            ConsoleUI.displayMessage("Error creating order: " + e.getMessage());
        }
    }
}