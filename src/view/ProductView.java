package view;

// Product listing and search

import java.util.Scanner;

public class ProductView {
    private Scanner scanner = new Scanner(System.in);

    public String getSearchInput() {
        System.out.print("[+] Enter product name, category, or first letter: ");
        return scanner.nextLine();
    }

    public void displayProducts(List<ProductDTO> products) {
        if (products.isEmpty()) {
            System.out.println("No matching products found.");
            return;
        }

        System.out.println("=== Search Results ===");
        for (ProductDTO product : products) {
            System.out.printf("• [%s] %s - $%.2f (%s)\n", product.getUuid(), product.getName(), product.getPrice(), product.getCategory());
        }
    }
}
