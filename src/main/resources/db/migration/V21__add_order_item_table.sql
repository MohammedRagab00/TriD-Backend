CREATE TABLE order_item
(
    order_id   INT            NOT NULL
        CONSTRAINT order_foreign_key
            REFERENCES orders
            ON DELETE CASCADE,
    variant_id INT            NOT NULL
        CONSTRAINT product_foreign_key
            REFERENCES product_variant
            ON DELETE CASCADE,
    quantity   INT            NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (order_id, variant_id)
);