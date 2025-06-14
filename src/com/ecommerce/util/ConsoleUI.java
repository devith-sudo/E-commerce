// ConsoleUI.java
package com.ecommerce.util;

import java.util.List;
import java.util.Map;

public class ConsoleUI {
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";
    private static final String BG_BLACK = "\u001B[40m";
    private static final String BG_RED = "\u001B[41m";
    private static final String BG_GREEN = "\u001B[42m";
    private static final String BG_YELLOW = "\u001B[43m";
    private static final String BG_BLUE = "\u001B[44m";
    private static final String BG_PURPLE = "\u001B[45m";
    private static final String BG_CYAN = "\u001B[46m";
    private static final String BG_WHITE = "\u001B[47m";

    public static void displayTable(String[] headers, List<Map<String, Object>> data) {
        // Calculate column widths
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
            for (Map<String, Object> row : data) {
                Object val = row.get(headers[i]);
                if (val != null) {
                    widths[i] = Math.max(widths[i], val.toString().length());
                }
            }
        }

        // Print colored header
        printColoredSeparator(widths, BG_BLUE, WHITE);
        printColoredRow(headers, widths, BG_BLUE, WHITE);
        printColoredSeparator(widths, BG_BLUE, WHITE);

        // Print data with alternating colors
        boolean alternate = false;
        for (Map<String, Object> row : data) {
            Object[] values = new Object[headers.length];
            for (int i = 0; i < headers.length; i++) {
                values[i] = row.get(headers[i]);
            }
            String bgColor = alternate ? BG_CYAN : BG_GREEN;
            printColoredRow(values, widths, bgColor, BLACK);
            alternate = !alternate;
        }
        printColoredSeparator(widths, BG_BLUE, WHITE);
    }

    public static void displayMenu(String title, String[] options) {
        // Colored menu box
        int width = title.length() + 4;
        for (String option : options) {
            width = Math.max(width, option.length() + 4);
        }

        System.out.println(BG_BLUE + WHITE + "╔" + "═".repeat(width) + "╗" + RESET);
        System.out.println(BG_BLUE + WHITE + "║" + centerText(title, width) + "║" + RESET);
        System.out.println(BG_BLUE + WHITE + "╠" + "═".repeat(width) + "╣" + RESET);

        for (int i = 0; i < options.length; i++) {
            String line = " " + (i + 1) + ". " + options[i];
            System.out.println(BG_BLUE + WHITE + "║" + String.format("%-" + width + "s", line) + "║" + RESET);
        }

        System.out.println(BG_BLUE + WHITE + "╚" + "═".repeat(width) + "╝" + RESET);
    }

    public static void displayMessage(String message) {
        int width = message.length() + 4;
        String border = BG_PURPLE + WHITE + "╔" + "═".repeat(width) + "╗" + RESET;
        String textLine = BG_PURPLE + WHITE + "║  " + message + "  ║" + RESET;
        String bottom = BG_PURPLE + WHITE + "╚" + "═".repeat(width) + "╝" + RESET;

        System.out.println(border);
        System.out.println(textLine);
        System.out.println(bottom);
    }

    private static void printColoredRow(Object[] values, int[] widths, String bgColor, String textColor) {
        StringBuilder sb = new StringBuilder(bgColor + textColor + "│" + RESET);
        for (int i = 0; i < values.length; i++) {
            sb.append(bgColor + textColor + " ").append(String.format("%-" + widths[i] + "s",
                            values[i] != null ? values[i].toString() : ""))
                    .append(" " + RESET + bgColor + textColor + "│" + RESET);
        }
        System.out.println(sb.toString());
    }

    private static void printColoredSeparator(int[] widths, String bgColor, String textColor) {
        StringBuilder sb = new StringBuilder(bgColor + textColor + "├" + RESET);
        for (int width : widths) {
            sb.append(bgColor + textColor + "─".repeat(width + 2) + "┼" + RESET);
        }
        sb.setCharAt(sb.length() - 3, '┤'); // Replace last ┼ with ┤
        System.out.println(sb.toString());
    }

    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
}