package service;

import dao.OrderDAO;
import dao.ProductDAO;
import model.Order;
import model.Product;

import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
    }

    public Order createOrder(Order order) {
        // Validate product quantities before creating order
        for (OrderProduct item : order.getOrderProducts()) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product == null || product.getQty() < item.getQuantity()) {
                throw new IllegalArgumentException("Invalid product or insufficient quantity for product ID: " + item.getProductId());
            }
        }

        // Update product quantities
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
}