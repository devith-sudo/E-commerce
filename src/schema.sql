-- Set schema path
-- Create Database Name "ecommerce"
SET search_path = ecommerce;

-- Users Table
CREATE TABLE Users (
                       user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categories Table
CREATE TABLE Categories (
                            category_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            category_name VARCHAR(100) NOT NULL
);

-- Products Table
CREATE TABLE Products (
                          product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name VARCHAR(100) NOT NULL,
                          category_id UUID REFERENCES Categories(category_id) ON DELETE SET NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders Table
CREATE TABLE Orders (
                        order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        user_id UUID REFERENCES Users(user_id) ON DELETE CASCADE,
                        order_code VARCHAR(50) UNIQUE NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        total_price DECIMAL(10, 2)
);

-- Order_Items Table
CREATE TABLE Order_Items (
                             order_item_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             order_id UUID REFERENCES Orders(order_id) ON DELETE CASCADE,
                             product_id UUID REFERENCES Products(product_id) ON DELETE SET NULL,
                             quantity INTEGER NOT NULL CHECK (quantity > 0),
                             price_per_item DECIMAL(10, 2) NOT NULL
);

-- Insert Random Users
INSERT INTO Users (username, password_hash)
SELECT
    'user_' || i,
    md5(random()::text)
FROM generate_series(1, 10) AS s(i);

-- Insert Categories
INSERT INTO Categories (category_name)
VALUES
    ('Electronics'),
    ('Clothing'),
    ('Books'),
    ('Home & Kitchen'),
    ('Toys');

-- Insert Random Products
INSERT INTO Products (name, category_id, price)
SELECT
    'Product_' || i,
    (SELECT category_id FROM Categories ORDER BY random() LIMIT 1),
    trunc(random() * 100 + 10, 2)
FROM generate_series(1, 20) AS s(i);

-- Insert Random Order Items
INSERT INTO Order_Items (order_id, product_id, quantity, price_per_item)
SELECT
    (SELECT order_id FROM Orders ORDER BY random() LIMIT 1),
    (SELECT product_id FROM Products ORDER BY random() LIMIT 1),
    floor(random() * 5 + 1)::int,
    trunc(random() * 100 + 5, 2)
FROM generate_series(1, 30) AS s(i);

-- Set schema
SET search_path = ecommerce;

-- Insert Users
INSERT INTO Users (user_id, username, password_hash, created_at) VALUES
                                                                     ('00000000-0000-0000-0000-000000000001', 'alice', 'hashed_pw_1', NOW()),
                                                                     ('00000000-0000-0000-0000-000000000002', 'bob', 'hashed_pw_2', NOW()),
                                                                     ('00000000-0000-0000-0000-000000000003', 'carol', 'hashed_pw_3', NOW());

-- Insert Categories
INSERT INTO Categories (category_id, category_name) VALUES
                                                        ('10000000-0000-0000-0000-000000000001', 'Electronics'),
                                                        ('10000000-0000-0000-0000-000000000002', 'Clothing'),
                                                        ('10000000-0000-0000-0000-000000000003', 'Books');

-- Insert Products
INSERT INTO Products (product_id, name, category_id, price, created_at) VALUES
                                                                            ('20000000-0000-0000-0000-000000000001', 'Smartphone', '10000000-0000-0000-0000-000000000001', 699.99, NOW()),
                                                                            ('20000000-0000-0000-0000-000000000002', 'T-Shirt', '10000000-0000-0000-0000-000000000002', 19.99, NOW()),
                                                                            ('20000000-0000-0000-0000-000000000003', 'Novel Book', '10000000-0000-0000-0000-000000000003', 12.50, NOW());

-- Insert Orders
INSERT INTO Orders (order_id, user_id, order_code, order_date, total_price) VALUES
                                                                                ('30000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'ORD00001', NOW(), 732.48),
                                                                                ('30000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'ORD00002', NOW(), 32.49);

-- Insert Order Items
INSERT INTO Order_Items (order_item_id, order_id, product_id, quantity, price_per_item) VALUES
                                                                                            ('40000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001', 1, 699.99),
                                                                                            ('40000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000003', 1, 12.50),
                                                                                            ('40000000-0000-0000-0000-000000000003', '30000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000002', 1, 19.99),
                                                                                            ('40000000-0000-0000-0000-000000000004', '30000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000003', 1, 12.50);

select *
from users;

select *
from categories;

select *
from order_items;

select *
from orders;

select *
from products;