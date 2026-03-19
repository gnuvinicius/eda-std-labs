CREATE SEQUENCE public.payments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.payments (
     id bigint PRIMARY KEY NOT NULL DEFAULT nextval('public.payments_id_seq'::regclass),
     payment_date timestamp(6) without time zone NULL,
     payment_method character varying(255) NOT NULL,
     payment_notes character varying(255) NULL,
     payment_status character varying(255) NULL,
     total_amount numeric(38, 2) NOT NULL,
     transaction_id character varying(255) NULL
);


CREATE SEQUENCE public.addresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.addresses (
      id bigint PRIMARY KEY NOT NULL DEFAULT nextval('public.addresses_id_seq'::regclass),
      city character varying(255) NULL,
      complement character varying(255) NULL,
      country character varying(255) NULL,
      neighborhood character varying(255) NULL,
      number character varying(255) NULL,
      state character varying(255) NULL,
      street character varying(255) NULL,
      zipcode character varying(255) NULL
);


CREATE TABLE public.orders (
       id uuid PRIMARY KEY NOT NULL,
       cancellation_reason character varying(255) NULL,
       customer_id uuid NULL,
       customer_notes character varying(255) NULL,
       delivered_at timestamp(6) without time zone NULL,
       last_status character varying(255) NULL,
       shipping_cost numeric(38, 2) NULL,
       status character varying(255) NULL,
       total_discount numeric(38, 2) NULL,
       created_at timestamp(6) without time zone NULL,
       updated_at timestamp(6) without time zone NULL,
       payment_id bigint NULL,
       shipping_address_id bigint NULL
);

ALTER TABLE public.orders
    ADD CONSTRAINT orders_payment_id_fkey FOREIGN KEY (payment_id)
        REFERENCES public.payments (id) MATCH SIMPLE -- Especifica que a chave estrangeira deve corresponder exatamente a um valor na tabela pai (payments).
        ON UPDATE NO ACTION -- Ação a ser tomada quando a chave primária da tabela pai for atualizada. NO ACTION significa que nenhuma ação será tomada, ou seja, a atualização será rejeitada se houver registros filhos que dependam da chave primária.
        ON DELETE CASCADE; -- Ação a ser tomada quando um registro da tabela pai for excluído. NO ACTION significa que nenhuma ação será tomada, ou seja, a exclusão será rejeitada se houver registros filhos que dependam da chave primária.
ALTER TABLE public.orders
    ADD CONSTRAINT orders_shipping_address_id_fkey FOREIGN KEY (shipping_address_id)
        REFERENCES public.addresses (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE;

CREATE SEQUENCE public.order_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.order_items (
    id bigint PRIMARY KEY NOT NULL default nextval('public.order_items_id_seq'::regclass),
    discount numeric(38, 2) NULL,
    product_id uuid NULL,
    product_name character varying(255) NULL,
    quantity integer NULL,
    unit_price numeric(38, 2) NULL,
    order_id uuid NULL
);

ALTER TABLE public.order_items
    ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id)
        REFERENCES public.orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE;
