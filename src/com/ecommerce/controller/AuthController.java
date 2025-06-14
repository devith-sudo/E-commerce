// AuthController.java
package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import com.ecommerce.util.ConsoleUI;
import com.ecommerce.util.InputValidator;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void showAuthMenu() {
        String[] options = {"Login", "Register", "Exit"};
        ConsoleUI.displayMenu("Authentication Menu", options);

        int choice = InputValidator.getValidInt("Enter your choice: ");
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                ConsoleUI.displayMessage("Invalid choice. Please try again.");
        }
    }

    private void login() {
        String username = InputValidator.getValidString("Enter username: ");
        String password = InputValidator.getValidString("Enter password: ");

        if (authService.login(username, password)) {
            ConsoleUI.displayMessage("Login successful!");
        } else {
            ConsoleUI.displayMessage("Invalid username or password.");
        }
    }

    private void register() {
        String username = InputValidator.getValidString("Enter username: ");
        String email = InputValidator.getValidEmail("Enter email: ");
        String password = InputValidator.getValidString("Enter password: ");

        User user = new User();
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);

        if (authService.register(user)) {
            ConsoleUI.displayMessage("Registration successful! You can now login.");
        } else {
            ConsoleUI.displayMessage("Username already exists. Please try another one.");
        }
    }

    public void logout() {
        authService.logout();
        ConsoleUI.displayMessage("Logged out successfully.");
    }
}