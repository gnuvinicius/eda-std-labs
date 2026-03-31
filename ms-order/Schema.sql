CREATE TABLE public.orders
(
    id                  uuid NOT NULL,
    cancellation_reason varchar(255) NULL,
    customer_id         uuid NULL,
    customer_notes      varchar(255) NULL,
    delivered_at        timestamp(6) NULL,
    last_status         varchar(255) NULL,
    shipping_cost       numeric(38, 2) NULL,
    status              varchar(255) NULL,
    total_discount      numeric(38, 2) NULL,
    created_at          timestamp(6) NULL,
    updated_at          timestamp(6) NULL,
    payment_id          int8 NULL,
    shipping_address_id int8 NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

------------------

ALTER TABLE public.orders
    ADD CONSTRAINT orders_payment_id_fkey
        FOREIGN KEY (payment_id)
            REFERENCES public.payments (id) ON DELETE CASCADE;
ALTER TABLE public.orders
    ADD CONSTRAINT orders_shipping_address_id_fkey
        FOREIGN KEY (shipping_address_id)
            REFERENCES public.addresses (id) ON DELETE CASCADE;

------------------

CREATE TABLE public.order_items
(
    id           bigserial NOT NULL,
    discount     numeric(38, 2) NULL,
    product_id   uuid NULL,
    product_name varchar(255) NULL,
    quantity     int4 NULL,
    unit_price   numeric(38, 2) NULL,
    order_id     uuid NULL,
    CONSTRAINT order_items_pkey PRIMARY KEY (id)
);

ALTER TABLE public.order_items
    ADD CONSTRAINT order_items_order_id_fkey
        FOREIGN KEY (order_id)
            REFERENCES public.orders (id) ON DELETE CASCADE;

-------------------

CREATE TABLE public.addresses
(
    id           bigserial NOT NULL,
    city         varchar(255) NULL,
    complement   varchar(255) NULL,
    country      varchar(255) NULL,
    neighborhood varchar(255) NULL,
    "number"     varchar(255) NULL,
    state        varchar(255) NULL,
    street       varchar(255) NULL,
    zipcode      varchar(255) NULL,
    CONSTRAINT addresses_pkey PRIMARY KEY (id)
);

--------------------

CREATE TABLE public.payments
(
    id             bigserial      NOT NULL,
    payment_date   timestamp(6) NULL,
    payment_method varchar(255)   NOT NULL,
    payment_notes  varchar(255) NULL,
    payment_status varchar(255) NULL,
    total_amount   numeric(38, 2) NOT NULL,
    transaction_id varchar(255) NULL,
    CONSTRAINT payments_pkey PRIMARY KEY (id)
);