package view;
// Main menu UI

import java.util.Scanner;

public class MainMenu {

    private Scanner scanner = new Scanner(System.in);

    public int showMainMenu(){
        System.out.println("==== Main Menu ====");
        System.out.println("1. View Products");
        System.out.println("2. Search Product");
        System.out.println("3. View Cart");
        System.out.println("4. Checkout");
        System.out.println("5. Logout");
        System.out.print("Choose option: ");
        return scanner.nextInt();
    }
}