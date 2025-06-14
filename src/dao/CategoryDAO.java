package dao;

import config.DatabaseConfig;
import model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryDAO {
    public Category createCategory(Category category) throws SQLException {
        String sql = "INSERT INTO Categories (category_id, category_name) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, category.getCategoryId());
            stmt.setString(2, category.getCategoryName());

            stmt.executeUpdate();
            return category;
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Category(
                        (UUID) rs.getObject("category_id"),
                        rs.getString("category_name")
                ));
            }
        }
        return categories;
    }

    public Category getCategoryById(UUID categoryId) throws SQLException {
        String sql = "SELECT * FROM Categories WHERE category_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            (UUID) rs.getObject("category_id"),
                            rs.getString("category_name")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateCategory(Category category) throws SQLException {
        String sql = "UPDATE Categories SET category_name = ? WHERE category_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setObject(2, category.getCategoryId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCategory(UUID categoryId) throws SQLException {
        String sql = "DELETE FROM Categories WHERE category_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, categoryId);
            return stmt.executeUpdate() > 0;
        }
    }
}