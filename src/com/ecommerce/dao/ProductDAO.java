// ProductDAO.java
package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConfig;
import com.ecommerce.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductDAO {
    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (p_name, price, qty, p_uuid) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getPName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQty());
            stmt.setString(4, UUID.randomUUID().toString());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating product failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }

            return product;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ? AND is_deleted = FALSE";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProduct(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting product by ID", e);
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE is_deleted = FALSE";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all products", e);
        }
        return products;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET p_name = ?, price = ?, qty = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getPName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQty());
            stmt.setInt(4, product.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public boolean deleteProduct(int productId) {
        String sql = "UPDATE products SET is_deleted = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    //add category column to table
    // No category in plain SELECT * FROM products, so don't call getString("category")
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setPName(rs.getString("p_name"));
        product.setPrice(rs.getDouble("price"));
        product.setQty(rs.getInt("qty"));
        product.setIsDeleted(rs.getBoolean("is_deleted"));
        product.setPUuid(rs.getString("p_uuid"));
        // Do NOT fetch category here unless you join with categories
        return product;
    }

    public List<Product> searchProductsByNameOrCategory(String keyword) {
        List<Product> products = new ArrayList<>();
        // Use LEFT JOIN to Also Include Products with NULL Category
        String sql = """
                    SELECT p.*, c.name AS category_name
                    FROM products p
                    LEFT JOIN categories c ON p.category_id = c.id
                    WHERE p.is_deleted = FALSE
                      AND (LOWER(p.p_name) LIKE ? OR LOWER(COALESCE(c.name, '')) LIKE ?)
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRowToProductWithCategory(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching products", e);
        }

        return products;
    }
    private Product mapRowToProductWithCategory(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setPName(rs.getString("p_name"));
        product.setPrice(rs.getDouble("price"));
        product.setQty(rs.getInt("qty"));
        product.setIsDeleted(rs.getBoolean("is_deleted"));
        product.setPUuid(rs.getString("p_uuid"));
        product.setCategory(rs.getString("category_name")); // << This is essential
        return product;
    }

}