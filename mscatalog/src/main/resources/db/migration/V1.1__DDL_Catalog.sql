-- V1.1__DDL_Catalog.sql

-- Tables for Brand and Category
CREATE TABLE brand (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES category(id)
);

-- Tables for Attributes
CREATE TABLE attribute (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE attribute_value (
    id UUID PRIMARY KEY,
    value VARCHAR(255) NOT NULL,
    attribute_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_attribute_value_attribute FOREIGN KEY (attribute_id) REFERENCES attribute(id)
);

-- Product Aggregate Root
CREATE TABLE product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    brand_id UUID NOT NULL,
    category_id UUID NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES brand(id),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id)
);

-- Value Object Tags (Element Collection)
CREATE TABLE product_tags (
    product_id UUID NOT NULL,
    tag VARCHAR(255) NOT NULL,
    CONSTRAINT fk_product_tags_product FOREIGN KEY (product_id) REFERENCES product(id)
);

-- ProductVariant Entity
CREATE TABLE product_variant (
    id UUID PRIMARY KEY,
    sku_code VARCHAR(255) NOT NULL UNIQUE,
    barcode VARCHAR(255),
    price_amount DECIMAL(19, 4) NOT NULL,
    price_currency VARCHAR(10) NOT NULL,
    promotional_price_amount DECIMAL(19, 4),
    promotional_price_currency VARCHAR(10),
    weight DOUBLE PRECISION,
    height DOUBLE PRECISION,
    width DOUBLE PRECISION,
    depth DOUBLE PRECISION,
    product_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Relationship between Variant and AttributeValues (Many-to-Many)
CREATE TABLE product_variant_attribute_values (
    variant_id UUID NOT NULL,
    attribute_value_id UUID NOT NULL,
    PRIMARY KEY (variant_id, attribute_value_id),
    CONSTRAINT fk_variant_attr_variant FOREIGN KEY (variant_id) REFERENCES product_variant(id),
    CONSTRAINT fk_variant_attr_value FOREIGN KEY (attribute_value_id) REFERENCES attribute_value(id)
);

-- Collection Entity
CREATE TABLE collection (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
