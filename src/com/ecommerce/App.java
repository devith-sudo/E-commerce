// App.java
package com.ecommerce;

import com.ecommerce.controller.*;
import com.ecommerce.dao.*;
import com.ecommerce.service.*;
import com.ecommerce.util.*;

public class App {
    public static void main(String[] args) {
        // Initialize DAOs
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        OrderDAO orderDAO = new OrderDAO();

        // Initialize services
        AuthService authService = new AuthService(userDAO);
        ProductService productService = new ProductService(productDAO);
        UserService userService = new UserService(userDAO);
        OrderService orderService = new OrderService(orderDAO, productDAO);

        // Initialize controllers
        AuthController authController = new AuthController(authService);
        ProductController productController = new ProductController(productService, authService);
        UserController userController = new UserController(userService, authService);
        OrderController orderController = new OrderController(orderService, productService, authService);

        // Main application loop
        while (true) {
            if (!authService.isLoggedIn()) {
                authController.showAuthMenu();
            } else {
                showMainMenu(authController, productController, orderController, userController, authService);
            }
        }
    }

    private static void showMainMenu(AuthController authController, ProductController productController,
                                     OrderController orderController, UserController userController,
                                     AuthService authService) {
        String[] options;
        if (authService.isAdmin()) {
            options = new String[]{"Products", "Orders", "Users", "Logout"};
        } else if (authService.isStaff()) {
            options = new String[]{"Products", "Orders", "Logout"};
        } else {
            options = new String[]{"Products", "Orders", "Logout"};
        }

        ConsoleUI.displayMenu("Main Menu", options);

        int choice = InputValidator.getValidInt("Enter your choice: ");
        switch (choice) {
            case 1:
                productController.showProductMenu();
                break;
            case 2:
                orderController.showOrderMenu();
                break;
            case 3:
                if (authService.isAdmin()) {
                    userController.showUserMenu();
                } else {
                    authController.logout();
                }
                break;
            case 4:
                if (authService.isAdmin() || authService.isStaff()) {
                    authController.logout();
                }
                break;
            default:
                ConsoleUI.displayMessage("Invalid choice. Please try again.");
        }
    }
}