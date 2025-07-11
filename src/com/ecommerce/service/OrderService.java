package com.ecommerce.service;

import com.ecommerce.dao.OrderDAO;
import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.OrderProduct;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    public Order createOrder(Order order) {
        for (OrderProduct item : order.getOrderProducts()) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product == null || product.getQty() < item.getQuantity()) {
                throw new IllegalArgumentException("Invalid product or insufficient quantity for product ID: " + item.getProductId());
            }
        }

        for (OrderProduct item : order.getOrderProducts()) {
            Product product = productDAO.getProductById(item.getProductId());
            product.setQty(product.getQty() - item.getQuantity());
            productDAO.updateProduct(product);
        }

        return orderDAO.createOrder(order);
    }

    public List<Order> getOrdersByUser(int userId) {
        return orderDAO.getOrdersByUser(userId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    public void updateOrder(Order order) {
        // Update product quantities
        for (OrderProduct item : order.getOrderProducts()) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product != null) {
                product.setQty(product.getQty() - item.getQuantity());
                productDAO.updateProduct(product);
            }
        }
        orderDAO.updateOrder(order);
    }
}