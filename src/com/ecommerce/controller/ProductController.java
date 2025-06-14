// ProductController.java
package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.ProductService;
import com.ecommerce.util.ConsoleUI;
import com.ecommerce.util.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {
    private final ProductService productService;
    private final AuthService authService;

    public ProductController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    public void showProductMenu() {
        String[] options;
        if (authService.isAdmin() || authService.isStaff()) {
            options = new String[]{"View Products", "Add Product", "Update Product", "Delete Product", "Back"};
        } else {
            options = new String[]{"View Products", "Back"};
        }

        ConsoleUI.displayMenu("Product Management", options);

        int choice = InputValidator.getValidInt("Enter your choice: ");
        switch (choice) {
            case 1:
                viewProducts();
                break;
            case 2:
                if (authService.isAdmin() || authService.isStaff()) {
                    addProduct();
                } else {
                    ConsoleUI.displayMessage("Invalid choice. Please try again.");
                }
                break;
            case 3:
                if (authService.isAdmin() || authService.isStaff()) {
                    updateProduct();
                } else {
                    ConsoleUI.displayMessage("Invalid choice. Please try again.");
                }
                break;
            case 4:
                if (authService.isAdmin() || authService.isStaff()) {
                    deleteProduct();
                } else {
                    ConsoleUI.displayMessage("Invalid choice. Please try again.");
                }
                break;
            case 5:
                return;
            default:
                ConsoleUI.displayMessage("Invalid choice. Please try again.");
        }
        showProductMenu();
    }

    public void viewProducts() {
        List<Product> products = productService.getAllProducts();
        List<Map<String, Object>> tableData = new ArrayList<>();

        for (Product product : products) {
            Map<String, Object> row = new HashMap<>();
            row.put("ID", product.getId());
            row.put("Name", product.getPName());
            row.put("Price", product.getPrice());
            row.put("Quantity", product.getQty());
            tableData.add(row);
        }

        ConsoleUI.displayTable(new String[]{"ID", "Name", "Price", "Quantity"}, tableData);
    }

    private void addProduct() {
        String name = InputValidator.getValidString("Enter product name: ");
        double price = InputValidator.getValidDouble("Enter product price: ");
        int quantity = InputValidator.getValidInt("Enter product quantity: ");

        Product product = new Product();
        product.setPName(name);
        product.setPrice(price);
        product.setQty(quantity);

        product = productService.createProduct(product);
        ConsoleUI.displayMessage("Product added successfully with ID: " + product.getId());
    }

    private void updateProduct() {
        viewProducts();
        int productId = InputValidator.getValidInt("Enter product ID to update: ");
        Product product = productService.getProductById(productId);

        if (product == null) {
            ConsoleUI.displayMessage("Product not found.");
            return;
        }

        String name = InputValidator.getValidString(String.format("Enter new name [%s]: ", product.getPName()));
        double price = InputValidator.getValidDouble(String.format("Enter new price [%.2f]: ", product.getPrice()));
        int quantity = InputValidator.getValidInt(String.format("Enter new quantity [%d]: ", product.getQty()));

        product.setPName(name);
        product.setPrice(price);
        product.setQty(quantity);

        if (productService.updateProduct(product)) {
            ConsoleUI.displayMessage("Product updated successfully.");
        } else {
            ConsoleUI.displayMessage("Failed to update product.");
        }
    }

    private void deleteProduct() {
        viewProducts();
        int productId = InputValidator.getValidInt("Enter product ID to delete: ");

        if (productService.deleteProduct(productId)) {
            ConsoleUI.displayMessage("Product deleted successfully.");
        } else {
            ConsoleUI.displayMessage("Failed to delete product or product not found.");
        }
    }
}