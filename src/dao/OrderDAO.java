package dao;

import config.DatabaseConfig;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDAO {
    public Order createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (order_id, user_id, order_code, order_date, total_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, order.getOrderId());
            stmt.setObject(2, order.getUserId());
            stmt.setString(3, order.getOrderCode());
            stmt.setTimestamp(4, order.getOrderDate());
            stmt.setDouble(5, order.getTotalPrice());

            stmt.executeUpdate();
            return order;
        }
    }

    public List<Order> getOrdersByUserId(UUID userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(new Order(
                            (UUID) rs.getObject("order_id"),
                            (UUID) rs.getObject("user_id"),
                            rs.getString("order_code"),
                            rs.getTimestamp("order_date"),
                            rs.getDouble("total_price")
                    ));
                }
            }
        }
        return orders;
    }

    public Order getOrderById(UUID orderId) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            (UUID) rs.getObject("order_id"),
                            (UUID) rs.getObject("user_id"),
                            rs.getString("order_code"),
                            rs.getTimestamp("order_date"),
                            rs.getDouble("total_price")
                    );
                }
            }
        }
        return null;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new Order(
                        (UUID) rs.getObject("order_id"),
                        (UUID) rs.getObject("user_id"),
                        rs.getString("order_code"),
                        rs.getTimestamp("order_date"),
                        rs.getDouble("total_price")
                ));
            }
        }
        return orders;
    }
}
