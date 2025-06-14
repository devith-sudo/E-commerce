// ConsoleUI.java
package com.ecommerce.util;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.List;
import java.util.Map;

public class ConsoleUI {
    public static void displayTable(String[] headers, List<Map<String, Object>> data) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(headers).setTextAlignment(TextAlignment.CENTER);
        table.addRule();

        for (Map<String, Object> row : data) {
            Object[] rowData = new Object[headers.length];
            for (int i = 0; i < headers.length; i++) {
                rowData[i] = row.get(headers[i]);
            }
            table.addRow(rowData);
            table.addRule();
        }

        System.out.println(table.render());
    }

    public static void displayMenu(String title, String[] options) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(title).setTextAlignment(TextAlignment.CENTER);
        table.addRule();

        for (int i = 0; i < options.length; i++) {
            table.addRow(i + 1 + ". " + options[i]);
            table.addRule();
        }

        System.out.println(table.render());
    }

    public static void displayMessage(String message) {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(message).setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        System.out.println(table.render());
    }
}