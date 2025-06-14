// InputValidator.java
package com.ecommerce.util;

import java.util.Scanner;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Please enter a non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static double getValidDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Please enter a non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String getValidString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String getValidEmail(String prompt) {
        while (true) {
            String email = getValidString(prompt);
            if (email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return email;
            }
            System.out.println("Invalid email format. Please try again.");
        }
    }
}