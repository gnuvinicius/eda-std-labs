-- V1.0__DDL_Order.sql

CREATE TABLE cart (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    customer_id UUID,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE cart_item (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    product_variant_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price_amount DECIMAL(19, 4) NOT NULL,
    unit_price_currency VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES cart(id)
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    cart_id UUID,
    customer_id UUID,
    status VARCHAR(30) NOT NULL,
    total_amount DECIMAL(19, 4) NOT NULL,
    total_currency VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE order_item (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    product_variant_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price_amount DECIMAL(19, 4) NOT NULL,
    unit_price_currency VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

