// OrderDAO.java
package com.ecommerce.dao;

import com.ecommerce.config.DatabaseConfig;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public Order createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

            // Insert order products
            insertOrderProducts(conn, order);

            return order;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating order", e);
        }
    }

    private void insertOrderProducts(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO order_products (order_id, product_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (OrderProduct item : order.getOrderProducts()) {
                stmt.setInt(1, order.getId());
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getPriceAtOrder());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapRowToOrder(rs);
                    order.setOrderProducts(getOrderProducts(conn, order.getId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting orders by user", e);
        }
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = mapRowToOrder(rs);
                order.setOrderProducts(getOrderProducts(conn, order.getId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all orders", e);
        }
        return orders;
    }

    private List<OrderProduct> getOrderProducts(Connection conn, int orderId) throws SQLException {
        List<OrderProduct> orderProducts = new ArrayList<>();
        String sql = "SELECT * FROM order_products WHERE order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setOrderId(rs.getInt("order_id"));
                    orderProduct.setProductId(rs.getInt("product_id"));
                    orderProduct.setQuantity(rs.getInt("quantity"));
                    orderProduct.setPriceAtOrder(rs.getDouble("price_at_order"));
                    orderProducts.add(orderProduct);
                }
            }
        }
        return orderProducts;
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return order;
    }

    private List<OrderProduct> getOrderProducts(Connection conn, int orderId) throws SQLException {
        List<OrderProduct> orderProducts = new ArrayList<>();
        String sql = "SELECT * FROM order_products WHERE order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setOrderId(rs.getInt("order_id"));
                    orderProduct.setProductId(rs.getInt("product_id"));
                    orderProduct.setQuantity(rs.getInt("quantity"));
                    orderProduct.setPriceAtOrder(rs.getDouble("price_at_order"));
                    orderProducts.add(orderProduct);
                }
            }
        }
        return orderProducts;
    }
}