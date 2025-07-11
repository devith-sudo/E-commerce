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
    private Order currentCart; // Cumulative cart for the session

    public OrderController(OrderService orderService, ProductService productService, AuthService authService) {
        this.orderService = orderService;
        this.productService = productService;
        this.authService = authService;
    }

    public void showOrderMenu() {
        String[] options;
        if (authService.isAdmin() || authService.isStaff()) {
            options = new String[]{"View My Orders", "View All Orders", "Create Order", "Checkout", "Back"};
        } else {
            options = new String[]{"View My Orders", "Create Order", "Checkout", "Back"};
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
                    createOrderMenu();
                }
                break;
            case 3:
                if (authService.isAdmin() || authService.isStaff()) {
                    createOrderMenu();
                } else {
                    checkout();
                }
                break;
            case 4:
                if (authService.isAdmin() || authService.isStaff()) {
                    checkout();
                } else {
                    return;
                }
                break;
            case 5:
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

    private void createOrderMenu() {
        if (currentCart == null) {
            initializeCart();
        }

        while (true) {
            ProductController productController = new ProductController(productService, authService);
            productController.viewProducts();

            String[] options = {"Add Product to Order", "View Cart", "Checkout", "Cancel"};
            ConsoleUI.displayMenu("Create Order", options);

            int choice = InputValidator.getValidInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    addProductToOrder();
                    break;
                case 2:
                    viewCart();
                    break;
                case 3:
                    checkout();
                    currentCart = null; // Reset after checkout
                    return;
                case 4:
                    currentCart = null; // Reset on cancel
                    return;
                default:
                    ConsoleUI.displayMessage("Invalid choice. Please try again.");
            }
        }
    }

    private void initializeCart() {
        if (authService.getCurrentUser() == null) {
            ConsoleUI.displayMessage("Please log in first.");
            return;
        }

        currentCart = new Order();
        currentCart.setUserId(authService.getCurrentUser().getId());
        currentCart.setOrderProducts(new ArrayList<>());
    }


    private void addProductToOrder() {
        if (currentCart == null) {
            initializeCart();
        }

        int productId = InputValidator.getValidInt("Enter product ID to add to order (0 to go back): ");
        if (productId == 0) return;

        Product product = productService.getProductById(productId);
        if (product == null) {
            ConsoleUI.displayMessage("Product not found.");
            return;
        }

        int quantity = InputValidator.getValidInt(String.format("Enter quantity (max %d): ", product.getQty()));
        if (quantity <= 0 || quantity > product.getQty()) {
            ConsoleUI.displayMessage("Invalid quantity.");
            return;
        }

        OrderProduct orderItem = new OrderProduct();
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);
        orderItem.setPriceAtOrder(product.getPrice());
        currentCart.getOrderProducts().add(orderItem);

        ConsoleUI.displayMessage("Product added to order.");
    }

    private void viewCart() {
        if (currentCart == null || currentCart.getOrderProducts().isEmpty()) {
            ConsoleUI.displayMessage("Your cart is empty.");
            return;
        }

        double totalAmount = currentCart.getOrderProducts().stream()
                .mapToDouble(item -> item.getPriceAtOrder() * item.getQuantity())
                .sum();

        System.out.println("\n" + "=".repeat(40));
        System.out.println("              YOUR CART");
        System.out.println("=".repeat(40));
        System.out.printf("%-20s %-8s %-8s\n", "Product", "Qty", "Price");

        for (OrderProduct item : currentCart.getOrderProducts()) {
            Product product = productService.getProductById(item.getProductId());
            System.out.printf("%-20s %-8d $%-7.2f\n",
                    product != null ? product.getPName() : "Unknown Product",
                    item.getQuantity(),
                    item.getPriceAtOrder());
        }

        System.out.println("-".repeat(40));
        System.out.printf("TOTAL: $%.2f\n", totalAmount);
        System.out.println("=".repeat(40) + "\n");
    }

    private void checkout() {
        if (currentCart == null || currentCart.getOrderProducts() == null || currentCart.getOrderProducts().isEmpty()) {
            ConsoleUI.displayMessage("No items in cart to checkout.");
            return;
        }

        if ("Completed".equalsIgnoreCase(currentCart.getStatus())) {
            ConsoleUI.displayMessage("This order has already been checked out.");
            return;
        }

        double totalAmount = currentCart.getOrderProducts().stream()
                .mapToDouble(item -> item.getPriceAtOrder() * item.getQuantity())
                .sum();
        currentCart.setTotalAmount(totalAmount);

        currentCart.setStatus("Completed");

        try {
            if (currentCart.getId() == null || currentCart.getId() == 0) {
                // Create a new order
                currentCart = orderService.createOrder(currentCart);
            } else {
                // Update existing order
                orderService.updateOrder(currentCart);
            }

            checkoutOrder(currentCart);

            currentCart = null;

        } catch (Exception e) {
            ConsoleUI.displayMessage("Error during checkout: " + e.getMessage());
            e.printStackTrace();
        }
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

    private void checkoutOrder(Order order) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("              RECEIPT");
        System.out.println("=".repeat(40));
        System.out.printf("Order ID   : %d\n", order.getId());
        System.out.printf("User ID    : %d\n", order.getUserId());
        System.out.println("-".repeat(40));
        System.out.printf("%-20s %-8s %-8s\n", "Product", "Qty", "Price");

        List<OrderProduct> orderProducts = order.getOrderProducts();
        if (orderProducts == null || orderProducts.isEmpty()) {
            System.out.println("No items in this order.");
        } else {
            for (OrderProduct item : orderProducts) {
                Product product = productService.getProductById(item.getProductId());
                System.out.printf("%-20s %-8d $%-7.2f\n",
                        product != null ? product.getPName() : "Unknown Product",
                        item.getQuantity(),
                        item.getPriceAtOrder());
            }
        }

        System.out.println("-".repeat(40));
        System.out.printf("TOTAL: $%.2f\n", order.getTotalAmount());
        System.out.println("Thank you for your purchase!");
        System.out.println("Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("=".repeat(40) + "\n");
    }
}