CREATE TABLE cart
(
    id                 INTEGER   NOT NULL PRIMARY KEY,
    user_id            INTEGER   NOT NULL
        CONSTRAINT user_foreign_key
            REFERENCES users ON DELETE CASCADE,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by         INTEGER   NOT NULL,
    last_modified_by   INTEGER
);

CREATE TABLE cart_item
(
    cart_id    INTEGER NOT NULL
        CONSTRAINT cart_foreign_key
            REFERENCES cart ON DELETE CASCADE,

    product_id INTEGER NOT NULL
        CONSTRAINT product_foreign_key
            REFERENCES product_variant ON DELETE CASCADE,
    quantity   INTEGER NOT NULL,
    PRIMARY KEY (cart_id, product_id)
);

CREATE TABLE wishlist
(
    id                 INTEGER   NOT NULL PRIMARY KEY,
    user_id            INTEGER   NOT NULL
        CONSTRAINT user_foreign_key
            REFERENCES users ON DELETE CASCADE,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP,
    created_by         INTEGER   NOT NULL,
    last_modified_by   INTEGER
);

CREATE TABLE wishlist_item
(
    wishlist_id INTEGER NOT NULL
        CONSTRAINT wishlist_foreign_key
            REFERENCES wishlist ON DELETE CASCADE,

    product_id  INTEGER NOT NULL
        CONSTRAINT product_foreign_key
            REFERENCES product ON DELETE CASCADE,
    PRIMARY KEY (wishlist_id, product_id)
);