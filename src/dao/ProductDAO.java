package dao;

import config.DatabaseConfig;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductDAO {
    public Product createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Products (product_id, name, category_id, price, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, product.getProductId());
            stmt.setString(2, product.getName());
            stmt.setObject(3, product.getCategoryId());
            stmt.setDouble(4, product.getPrice());
            stmt.setTimestamp(5, product.getCreatedAt());

            stmt.executeUpdate();
            return product;
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        (UUID) rs.getObject("product_id"),
                        rs.getString("name"),
                        (UUID) rs.getObject("category_id"),
                        rs.getDouble("price"),
                        rs.getTimestamp("created_at")
                ));
            }
        }
        return products;
    }

    public Product getProductById(UUID productId) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            (UUID) rs.getObject("product_id"),
                            rs.getString("name"),
                            (UUID) rs.getObject("category_id"),
                            rs.getDouble("price"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE Products SET name = ?, category_id = ?, price = ? WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setObject(2, product.getCategoryId());
            stmt.setDouble(3, product.getPrice());
            stmt.setObject(4, product.getProductId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(UUID productId) throws SQLException {
        String sql = "DELETE FROM Products WHERE product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }
}