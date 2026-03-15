CREATE TABLE IF NOT EXISTS carts (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    checked_out_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id UUID NOT NULL,
    product_id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_cart_items_cart_id FOREIGN KEY (cart_id)
        REFERENCES carts (id)
        ON DELETE CASCADE,
    CONSTRAINT ck_cart_items_quantity_positive CHECK (quantity > 0),
    CONSTRAINT ck_cart_items_unit_price_non_negative CHECK (unit_price >= 0)
);

