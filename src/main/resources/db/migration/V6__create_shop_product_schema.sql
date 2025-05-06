CREATE TABLE coordinates (
    id INTEGER PRIMARY KEY,
    x_pos DOUBLE PRECISION NOT NULL,
    y_pos DOUBLE PRECISION NOT NULL,
    z_pos DOUBLE PRECISION NOT NULL,
    x_scale DOUBLE PRECISION NOT NULL,
    y_scale DOUBLE PRECISION NOT NULL,
    z_scale DOUBLE PRECISION NOT NULL,
    x_rot DOUBLE PRECISION NOT NULL,
    y_rot DOUBLE PRECISION NOT NULL,
    z_rot DOUBLE PRECISION NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP
);

CREATE TABLE shop_detail (
    id INTEGER PRIMARY KEY,
    name VARCHAR(40) NOT NULL UNIQUE,
    category VARCHAR(40),
    location VARCHAR(255),
    description VARCHAR(1000) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(255),
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by INTEGER NOT NULL,
    last_modified_by INTEGER
);

CREATE TABLE shop (
    id INTEGER PRIMARY KEY,
    owner_id INTEGER,
    shop_detail_id INTEGER,
    gltf VARCHAR(255),
    bin VARCHAR(255),
    icon VARCHAR(255),
    texture VARCHAR(255),
    coordinates_id INTEGER,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by INTEGER NOT NULL,
    last_modified_by INTEGER,
    FOREIGN KEY (coordinates_id) REFERENCES coordinates(id),
    FOREIGN KEY (shop_detail_id) REFERENCES shop_detail(id),
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE social (
    id INTEGER PRIMARY KEY,
    platform VARCHAR(255),
    link VARCHAR(255) UNIQUE,
    shop_id INTEGER,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (shop_id) REFERENCES shop_detail(id)
);

CREATE TABLE product_detail (
    id INTEGER PRIMARY KEY,
    name VARCHAR(255),
    sizes VARCHAR(255),
    colors VARCHAR(255),
    description TEXT,
    base_price DOUBLE PRECISION,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by INTEGER NOT NULL,
    last_modified_by INTEGER
);

CREATE TABLE product (
    id INTEGER PRIMARY KEY,
    shop_id INTEGER,
    details_id INTEGER,
    gltf VARCHAR(255),
    bin VARCHAR(255),
    icon VARCHAR(255),
    texture VARCHAR(255),
    coordinates_id INTEGER,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by INTEGER NOT NULL,
    last_modified_by INTEGER,
    FOREIGN KEY (shop_id) REFERENCES shop(id),
    FOREIGN KEY (details_id) REFERENCES product_detail(id),
    FOREIGN KEY (coordinates_id) REFERENCES coordinates(id)
);

CREATE TABLE product_variant (
    id INTEGER PRIMARY KEY,
    product_id INTEGER,
    color VARCHAR(255),
    size VARCHAR(20),
    stock INTEGER,
    price DOUBLE PRECISION,
    created_date TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product_detail(id),
    CONSTRAINT uk_product_variant_color_size UNIQUE (color, size)
);

CREATE INDEX idx_shop_owner ON shop(owner_id);
CREATE INDEX idx_product_shop ON product(shop_id);
CREATE INDEX idx_product_details ON product(details_id);
CREATE INDEX idx_variant_product ON product_variant(product_id);
CREATE INDEX idx_social_shop ON social(shop_id);