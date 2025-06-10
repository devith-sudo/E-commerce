package view;

// Login/Register UI

import java.util.Scanner;

public class AuthView {

    private Scanner scanner = new Scanner(System.in);

    public int showAuthView() {
        System.out.println("====== Authentication Menu ======");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.println("Choose an option: ");
        return scanner.nextInt();
    }

    public LoginRequestDTO getLoginInput(){
        scanner.nextLine();
        System.out.println("[+] Email: ");
        String email = scanner.nextLine();
        System.out.println("[+] Password: ");
        String password = scanner.nextLine();
        return new LoginRequestDTO(email, password);
    }

    public RegisterRequestDTO getRegisterInput() {
        scanner.nextLine(); // consume newline
        System.out.print("[+] Name: ");
        String name = scanner.nextLine();
        System.out.print("[+] Email: ");
        String email = scanner.nextLine();
        System.out.print("[+] Password: ");
        String password = scanner.nextLine();
        return new RegisterRequestDTO(name, email, password);
    }

    public void showLoginSuccess(String name) {
        System.out.println("Welcome, " + name + "!");
    }

    public void showError(String message) {
        System.out.println("❌ " + message);
    }
}
