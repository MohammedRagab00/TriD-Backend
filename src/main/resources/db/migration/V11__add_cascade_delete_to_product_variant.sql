ALTER TABLE product_variant
    DROP CONSTRAINT IF EXISTS fk_product_variant_product,
    ADD CONSTRAINT fk_product_variant_product
        FOREIGN KEY (product_id)
            REFERENCES product (id)
            ON DELETE CASCADE;