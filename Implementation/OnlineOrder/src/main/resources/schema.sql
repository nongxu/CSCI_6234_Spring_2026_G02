-- Users (base table for inheritance)
CREATE TABLE IF NOT EXISTS users (
    user_id   BIGSERIAL    PRIMARY KEY,
    email     VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL
);

-- Customers (extends User)
CREATE TABLE IF NOT EXISTS customers (
    user_id      BIGINT      PRIMARY KEY REFERENCES users(user_id),
    phone_number VARCHAR(20)
);

-- Restaurant Owners (extends User)
CREATE TABLE IF NOT EXISTS restaurant_owners (
    user_id       BIGINT       PRIMARY KEY REFERENCES users(user_id),
    business_name VARCHAR(255)
);

-- Restaurants
CREATE TABLE IF NOT EXISTS restaurants (
    restaurant_id BIGSERIAL    PRIMARY KEY,
    owner_id      BIGINT       NOT NULL REFERENCES restaurant_owners(user_id),
    name          VARCHAR(255) NOT NULL,
    address       VARCHAR(255),
    phone         VARCHAR(20),
    image         VARCHAR(500)
);

-- Menu Items
CREATE TABLE IF NOT EXISTS menu_items (
    menu_item_id  BIGSERIAL      PRIMARY KEY,
    restaurant_id BIGINT         NOT NULL REFERENCES restaurants(restaurant_id),
    name          VARCHAR(255)   NOT NULL,
    description   TEXT,
    price         DECIMAL(10, 2) NOT NULL,
    image         VARCHAR(500)
);

-- Carts
CREATE TABLE IF NOT EXISTS carts (
    cart_id     BIGSERIAL    PRIMARY KEY,
    customer_id BIGINT       NOT NULL UNIQUE REFERENCES customers(user_id),
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE'
);

-- Cart Items
CREATE TABLE IF NOT EXISTS cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id      BIGINT    NOT NULL REFERENCES carts(cart_id),
    menu_item_id BIGINT    NOT NULL REFERENCES menu_items(menu_item_id),
    quantity     INTEGER   NOT NULL CHECK (quantity > 0)
);

-- Orders
CREATE TABLE IF NOT EXISTS orders (
    order_id    BIGSERIAL      PRIMARY KEY,
    customer_id BIGINT         NOT NULL REFERENCES customers(user_id),
    total_price DECIMAL(10, 2) NOT NULL,
    status      VARCHAR(50)    NOT NULL,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);

-- Order Items
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id BIGSERIAL      PRIMARY KEY,
    order_id      BIGINT         NOT NULL REFERENCES orders(order_id),
    menu_item_id  BIGINT         NOT NULL REFERENCES menu_items(menu_item_id),
    quantity      INTEGER        NOT NULL CHECK (quantity > 0),
    price         DECIMAL(10, 2) NOT NULL
);
