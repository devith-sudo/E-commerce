// UserController.java
package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.UserService;
import com.ecommerce.util.ConsoleUI;
import com.ecommerce.util.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    public void showUserMenu() {
        if (!authService.isAdmin()) {
            ConsoleUI.displayMessage("Access denied. Admin privileges required.");
            return;
        }

        String[] options = {"View All Users", "Delete User", "Back"};
        ConsoleUI.displayMenu("User Management", options);

        int choice = InputValidator.getValidInt("Enter your choice: ");
        switch (choice) {
            case 1:
                viewAllUsers();
                break;
            case 2:
                deleteUser();
                break;
            case 3:
                return;
            default:
                ConsoleUI.displayMessage("Invalid choice. Please try again.");
        }
        showUserMenu();
    }

    private void viewAllUsers() {
        List<User> users = userService.getAllUsers();
        List<Map<String, Object>> tableData = new ArrayList<>();

        for (User user : users) {
            Map<String, Object> row = new HashMap<>();
            row.put("ID", user.getId());
            row.put("Username", user.getUserName());
            row.put("Email", user.getEmail());
            row.put("Role", user.getRole());
            tableData.add(row);
        }

        ConsoleUI.displayTable(new String[]{"ID", "Username", "Email", "Role"}, tableData);
    }

    private void deleteUser() {
        viewAllUsers();
        int userId = InputValidator.getValidInt("Enter user ID to delete: ");

        if (userId == authService.getCurrentUser().getId()) {
            ConsoleUI.displayMessage("You cannot delete your own account.");
            return;
        }

        if (userService.deleteUser(userId)) {
            ConsoleUI.displayMessage("User deleted successfully.");
        } else {
            ConsoleUI.displayMessage("Failed to delete user or user not found.");
        }
    }
}