ALTER TABLE orders
    ADD COLUMN address      VARCHAR(200),
    ADD COLUMN latitude     NUMERIC(9, 6),
    ADD COLUMN longitude    NUMERIC(9, 6),
    ADD COLUMN phone_number VARCHAR(15),
    ADD COLUMN landmark     VARCHAR(100);