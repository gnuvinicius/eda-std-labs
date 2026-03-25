CREATE TABLE public.orders
(
    id             bigserial NOT NULL,
    order_id       uuid      NOT NULL,
    customer_id    uuid      NOT NULL,
    checked_out_at timestamp without time zone NULL,
    CONSTRAINT carts_pkey PRIMARY KEY (id)
);

---

CREATE TABLE public.order_items
(
    id           bigserial              NOT NULL,
    order_id     bigserial              NOT NULL,
    product_id   uuid                   NOT NULL,
    product_name character varying(255) NOT NULL,
    quantity     integer                NOT NULL,
    unit_price   numeric(12, 2)         NOT NULL,
    CONSTRAINT order_items_pkey PRIMARY KEY (id),
    CONSTRAINT ck_order_items_quantity_positive CHECK ((quantity > 0)),
    CONSTRAINT ck_order_items_unit_price_non_negative CHECK ((unit_price >= (0)::numeric))
);

ALTER TABLE public.order_items
    ADD CONSTRAINT fk_order_items_order_id FOREIGN KEY (order_id)
        REFERENCES public.orders (id) ON DELETE CASCADE;



