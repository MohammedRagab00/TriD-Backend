ALTER TABLE product_variant
    DROP CONSTRAINT uk_product_variant_color_size,
    ADD CONSTRAINT uk_product_variant_color_size UNIQUE (product_id, color, size);