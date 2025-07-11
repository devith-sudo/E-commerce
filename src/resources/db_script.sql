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







--------------------------------
SET search_path TO ecommerce;

SELECT
    * FROM ecommerce.products;

SELECT id, p_name, price, qty, is_deleted FROM ecommerce.products;
INSERT INTO ecommerce.products (p_name, price, qty, p_uuid)
VALUES ('Laptop ASUS', 499.99, 10, gen_random_uuid());

DELETE FROM ecommerce.products
WHERE id NOT IN (
    SELECT MIN(id)
    FROM ecommerce.products
    GROUP BY p_name
);

-- add unique constraint
ALTER TABLE ecommerce.products
    ADD CONSTRAINT unique_pname UNIQUE (p_name);

INSERT INTO ecommerce.products (p_name, price, qty, p_uuid)
VALUES
    ('Apple iPhone 14', 799.99, 15, gen_random_uuid()),
    ('Samsung Galaxy S23', 749.50, 20, gen_random_uuid()),
    ('MacBook Pro 14"', 1999.99, 5, gen_random_uuid()),
    ('Dell XPS 13', 1299.49, 8, gen_random_uuid()),
    ('Sony WH-1000XM4 Headphones', 349.99, 25, gen_random_uuid()),
    ('Logitech MX Master 3 Mouse', 99.99, 30, gen_random_uuid()),
    ('Acer Aspire 7', 599.99, 12, gen_random_uuid());

ALTER TABLE ecommerce.products
    ADD COLUMN category VARCHAR(50);

UPDATE ecommerce.products SET category = 'Smartphones' WHERE p_name ILIKE '%iPhone%' OR p_name ILIKE '%Samsung%';
UPDATE ecommerce.products SET category = 'Laptops' WHERE p_name ILIKE '%MacBook%' OR p_name ILIKE '%Dell%' OR p_name ILIKE '%Acer%';
UPDATE ecommerce.products SET category = 'Accessories' WHERE p_name ILIKE '%Mouse%' OR p_name ILIKE '%Headphones%';

-- Create categories table
CREATE TABLE categories (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL UNIQUE
);
----------------------------------------------
-- Add category_id to products table (foreign key)
ALTER TABLE products
    ADD COLUMN category_id INTEGER REFERENCES categories(id);

ALTER TABLE ecommerce.products
    ADD COLUMN category_id INTEGER REFERENCES ecommerce.categories(id);


SELECT id, p_name, category_id FROM products WHERE category_id IS NULL;



