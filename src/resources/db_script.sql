-- db_scripts.sql
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       user_name VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
                       is_deleted BOOLEAN DEFAULT FALSE,
                       u_uuid VARCHAR(36) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          p_name VARCHAR(100) NOT NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          qty INTEGER NOT NULL,
                          is_deleted BOOLEAN DEFAULT FALSE,
                          p_uuid VARCHAR(36) NOT NULL UNIQUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        user_id INTEGER NOT NULL REFERENCES users(id),
                        total_amount DECIMAL(10, 2) NOT NULL,
                        status VARCHAR(20) DEFAULT 'PENDING',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_products (
                                order_id INTEGER NOT NULL REFERENCES orders(id),
                                product_id INTEGER NOT NULL REFERENCES products(id),
                                quantity INTEGER NOT NULL,
                                price_at_order DECIMAL(10, 2) NOT NULL,
                                PRIMARY KEY (order_id, product_id)
);