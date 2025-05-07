DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS shop CASCADE;
DROP TABLE IF EXISTS product_detail CASCADE;
DROP TABLE IF EXISTS shop_detail CASCADE;

CREATE TABLE model_asset
(
    id                 INTEGER PRIMARY KEY,
    gltf               VARCHAR(255),
    bin                VARCHAR(255),
    icon               VARCHAR(255),
    texture            VARCHAR(255),
    coordinates_id     INTEGER REFERENCES coordinates,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by         INTEGER   NOT NULL,
    last_modified_by   INTEGER
);

CREATE TABLE shop
(
    id                 INTEGER PRIMARY KEY,
    name               VARCHAR(40)   NOT NULL UNIQUE,
    category           VARCHAR(40),
    location           VARCHAR(255),
    description        VARCHAR(1000) NOT NULL,
    email              VARCHAR(60),
    phone              VARCHAR(20),
    owner_id           INTEGER REFERENCES users ON DELETE CASCADE,
    model_asset_id     INTEGER REFERENCES model_asset,
    created_date       TIMESTAMP     NOT NULL,
    last_modified_date TIMESTAMP,
    created_by         INTEGER       NOT NULL,
    last_modified_by   INTEGER
);

CREATE TABLE product
(
    id                 INTEGER PRIMARY KEY,
    shop_id            INTEGER REFERENCES shop,
    name               VARCHAR(255),
    sizes              VARCHAR(255),
    colors             VARCHAR(255),
    description        TEXT,
    base_price         DOUBLE PRECISION,
    model_asset_id     INTEGER REFERENCES model_asset,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by         INTEGER   NOT NULL,
    last_modified_by   INTEGER
);

CREATE INDEX idx_shop_owner ON shop (owner_id);
CREATE INDEX idx_product_shop ON product (shop_id);