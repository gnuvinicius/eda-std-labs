CREATE TABLE IF NOT EXISTS customers (
    id          UUID         PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    phone       VARCHAR(30),
    document    VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP,

    CONSTRAINT uq_customers_email    UNIQUE (email),
    CONSTRAINT uq_customers_document UNIQUE (document)
);

ALTER TABLE carts
    ADD CONSTRAINT fk_carts_customer_id
        FOREIGN KEY (customer_id)
        REFERENCES customers (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

