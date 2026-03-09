-- Table: brand
CREATE TABLE brand (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Table: category (self-referencing parent)
CREATE TABLE category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES category(id)
);

-- Table: attribute
CREATE TABLE attribute (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Table: attribute_value (references attribute)
CREATE TABLE attribute_value (
    id UUID PRIMARY KEY,
    value VARCHAR(255) NOT NULL,
    attribute_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_attribute_value_attribute FOREIGN KEY (attribute_id) REFERENCES attribute(id)
);

-- Table: product
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

-- Element collection: product_tags
CREATE TABLE product_tags (
    product_id UUID NOT NULL,
    tag VARCHAR(255) NOT NULL,
    CONSTRAINT fk_product_tags_product FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Table: product_variant
CREATE TABLE product_variant (
    id UUID PRIMARY KEY,
    sku_code VARCHAR(255) NOT NULL UNIQUE,
    barcode VARCHAR(255),
    price_amount NUMERIC(19,4) NOT NULL,
    price_currency VARCHAR(10) NOT NULL,
    promotional_price_amount NUMERIC(19,4),
    promotional_price_currency VARCHAR(10),
    width DOUBLE PRECISION,
    height DOUBLE PRECISION,
    depth DOUBLE PRECISION,
    product_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Join table: product_variant_attribute_values (many-to-many)
CREATE TABLE product_variant_attribute_values (
    variant_id UUID NOT NULL,
    attribute_value_id UUID NOT NULL,
    PRIMARY KEY (variant_id, attribute_value_id),
    CONSTRAINT fk_variant_attr_variant FOREIGN KEY (variant_id) REFERENCES product_variant(id),
    CONSTRAINT fk_variant_attr_value FOREIGN KEY (attribute_value_id) REFERENCES attribute_value(id)
);

-- Table: collection
CREATE TABLE collection (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Join table: collection_products (many-to-many collection <-> product)
CREATE TABLE collection_products (
    collection_id UUID NOT NULL,
    product_id UUID NOT NULL,
    PRIMARY KEY (collection_id, product_id),
    CONSTRAINT fk_collection_product_collection FOREIGN KEY (collection_id) REFERENCES collection(id),
    CONSTRAINT fk_collection_product_product FOREIGN KEY (product_id) REFERENCES product(id)
);
