// ProductService.java
package com.ecommerce.service;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product createProduct(Product product) {
        return productDAO.createProduct(product);
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }
}