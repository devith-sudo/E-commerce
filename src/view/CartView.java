package view;
// View and manage cart

import dto.CartItemDTO;

import java.util.List;
import java.util.Scanner;

public class CartView {
    private Scanner scanner = new Scanner(System.in);

    public void showCartItems(List<CartItemDTO> cartItems) {
        if (cartItems.isEmpty()) {
            System.out.println("🛒 Your cart is empty.");
            return;
        }

        System.out.println("=== 🛒 Cart Items ===");
        for (CartItemDTO item : cartItems) {
            System.out.printf("- %s x%d | $%.2f each | Total: $%.2f\n",
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotalPrice());
        }
    }

    public String promptProductUUIDToAdd() {
        System.out.print("Enter product UUID to add to cart: ");
        return scanner.nextLine();
    }

    public int promptQuantity() {
        System.out.print("Enter quantity: ");
        return scanner.nextInt();
    }

    public String promptProductUUIDToRemove() {
        scanner.nextLine();
        System.out.print("Enter product UUID to remove from cart: ");
        return scanner.nextLine();
    }

    public int showCartMenu() {
        System.out.println("1. Add product to cart");
        System.out.println("2. Remove product from cart");
        System.out.println("3. Proceed to checkout");
        System.out.println("4. Back to main menu");
        System.out.print("Choose an option: ");
        return scanner.nextInt();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}
