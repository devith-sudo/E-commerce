package controller;

import view.CartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainController {
    private final AuthController authController;
    private final ProductController productController;
    private final OrderController orderController;
    private final AdminController adminController;
    private final CartService cartService;
    private final CartView cartView;
    private final MenuView menuView;

    public MainController(AuthController authController, ProductController productController,
                          OrderController orderController, AdminController adminController,
                          CartService cartService, CartView cartView, MenuView menuView) {
        this.authController = authController;
        this.productController = productController;
        this.orderController = orderController;
        this.adminController = adminController;
        this.cartService = cartService;
        this.cartView = cartView;
        this.menuView = menuView;
    }

    public void run() {
        menuView.showWelcomeMessage();

        while (true) {
            if (!SessionService.isLoggedIn()) {
                showUnauthenticatedMenu();
            } else {
                showAuthenticatedMenu();
            }
        }
    }

    private void showUnauthenticatedMenu() {
        menuView.showMainMenu();
        int choice = InputValidator.getValidInt("", 1, 3);

        switch (choice) {
            case 1:
                authController.login();
                break;
            case 2:
                authController.register();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                menuView.showInvalidChoice();
        }
    }

    private void showAuthenticatedMenu() {
        if (SessionService.isAdmin()) {
            showAdminMenu();
        } else if (SessionService.isStaff()) {
            showStaffMenu();
        } else {
            showUserMenu();
        }
    }

    private void showUserMenu() {
        menuView.showUserMenu();
        int choice = InputValidator.getValidInt("", 1, 4);

        switch (choice) {
            case 1:
                productController.listProducts();
                break;
            case 2:
                showCartMenu();
                break;
            case 3:
                orderController.viewOrderHistory();
                break;
            case 4:
                authController.logout();
                break;
        }
    }

    private void showStaffMenu() {
        menuView.showStaffMenu();
        int choice = InputValidator.getValidInt("", 1, 4);

        switch (choice) {
            case 1:
                productController.listProducts();
                break;
            case 2:
                showProductManagementMenu();
                break;
            case 3:
                orderController.viewOrderHistory();
                break;
            case 4:
                authController.logout();
                break;
        }
    }

    private void showAdminMenu() {
        menuView.showAdminMenu();
        int choice = InputValidator.getValidInt("", 1, 4);

        switch (choice) {
            case 1:
                showProductManagementMenu();
                break;
            case 2:
                showUserManagementMenu();
                break;
            case 3:
                orderController.viewOrderHistory();
                break;
            case 4:
                authController.logout();
                break;
        }
    }

    private void showProductManagementMenu() {
        menuView.showProductManagementMenu();
        int choice = InputValidator.getValidInt("", 1, 4);

        switch (choice) {
            case 1:
                productController.createProduct();
                break;
            case 2:
                UUID productId = cartView.promptForProductId();
                productController.updateProduct(productId);
                break;
            case 3:
                productId = cartView.promptForProductId();
                productController.deleteProduct(productId);
                break;
            case 4:
                return; // Back to previous menu
        }
    }

    private void showUserManagementMenu() {
        menuView.showUserManagementMenu();
        int choice = InputValidator.getValidInt("", 1, 4);

        switch (choice) {
            case 1:
                adminController.listAllUsers();
                break;
            case 2:
                adminController.updateUserRole();
                break;
            case 3:
                adminController.deleteUser();
                break;
            case 4:
                return; // Back to previous menu
        }
    }

    private void showCartMenu() {
        while (true) {
            try {
                Map<Product, Integer> cartItems = cartService.getCartItems();
                double total = cartService.calculateTotal();
                cartView.displayCart(cartItems, total);

                menuView.showCartMenu();
                int choice = InputValidator.getValidInt("", 1, 6);

                switch (choice) {
                    case 1:
                        // Cart is already displayed
                        break;
                    case 2:
                        UUID productId = cartView.promptForProductId();
                        int quantity = cartView.promptForQuantity();
                        cartService.addToCart(productId, quantity);
                        cartView.displayAddToCartSuccess();
                        break;
                    case 3:
                        productId = cartView.promptForProductId();
                        cartService.removeFromCart(productId);
                        cartView.displayRemoveFromCartSuccess();
                        break;
                    case 4:
                        productId = cartView.promptForProductId();
                        quantity = cartView.promptForQuantity();
                        cartService.updateQuantity(productId, quantity);
                        cartView.displayUpdateQuantitySuccess();
                        break;
                    case 5:
                        if (cartItems.isEmpty()) {
                            cartView.displayError("Cart is empty");
                            break;
                        }
                        if (cartView.confirmCheckout(total)) {
                            checkout();
                        }
                        break;
                    case 6:
                        return; // Back to previous menu
                }
            } catch (Exception e) {
                cartView.displayError(e.getMessage());
            }
        }
    }

    private void checkout() throws Exception {
        Map<Product, Integer> cartItems = cartService.getCartItems();
        User currentUser = SessionService.getCurrentUser();

        List<OrderItemDTO> items = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            items.add(new OrderItemDTO(entry.getKey().getProductId(), entry.getValue()));
        }

        OrderDTO orderDTO = new OrderDTO(currentUser.getUserId(), items);
        orderController.checkout(orderDTO);
        cartService.clearCart();
    }
}
