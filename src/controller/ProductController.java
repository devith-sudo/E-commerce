package controller;

import model.Product;
import view.ProductView;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProductController {
    private final ProductService productService;
    private final ProductView productView;

    public ProductController(ProductService productService, ProductView productView) {
        this.productService = productService;
        this.productView = productView;
    }

    public void listProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            productView.displayProducts(products);
        } catch (SQLException e) {
            productView.displayError("Error fetching products: " + e.getMessage());
        }
    }

    public void viewProductDetails(UUID productId) {
        try {
            Product product = productService.getProductById(productId);
            if (product != null) {
                productView.displayProductDetails(product);
            } else {
                productView.displayError("Product not found");
            }
        } catch (SQLException e) {
            productView.displayError("Error fetching product details: " + e.getMessage());
        }
    }

    public void addProductToCart(UUID productId, int quantity) {
        // This would interact with CartService
        productView.displayMessage("Product added to cart");
    }

    public void createProduct() {
        if (!SessionService.isAdmin() && !SessionService.isStaff()) {
            productView.displayError("Unauthorized access");
            return;
        }

        try {
            Product product = productView.getNewProductDetails();
            Product createdProduct = productService.createProduct(product);
            productView.displayProductCreationSuccess(createdProduct);
        } catch (Exception e) {
            productView.displayError("Error creating product: " + e.getMessage());
        }
    }

    public void updateProduct(UUID productId) {
        if (!SessionService.isAdmin() && !SessionService.isStaff()) {
            productView.displayError("Unauthorized access");
            return;
        }

        try {
            Product product = productService.getProductById(productId);
            if (product == null) {
                productView.displayError("Product not found");
                return;
            }

            Product updatedDetails = productView.getUpdatedProductDetails(product);
            boolean success = productService.updateProduct(updatedDetails);
            if (success) {
                productView.displayProductUpdateSuccess();
            } else {
                productView.displayError("Failed to update product");
            }
        } catch (Exception e) {
            productView.displayError("Error updating product: " + e.getMessage());
        }
    }

    public void deleteProduct(UUID productId) {
        if (!SessionService.isAdmin() && !SessionService.isStaff()) {
            productView.displayError("Unauthorized access");
            return;
        }

        try {
            boolean success = productService.deleteProduct(productId);
            if (success) {
                productView.displayProductDeletionSuccess();
            } else {
                productView.displayError("Failed to delete product");
            }
        } catch (Exception e) {
            productView.displayError("Error deleting product: " + e.getMessage());
        }
    }
}
