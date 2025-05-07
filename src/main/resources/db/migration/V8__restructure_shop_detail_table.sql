TRUNCATE TABLE shop CASCADE ;

ALTER TABLE shop
    DROP COLUMN shop_detail_id,
    DROP CONSTRAINT shop_coordinates_id_fkey;

TRUNCATE TABLE social;

DROP TABLE shop_detail CASCADE;

CREATE TABLE shop_detail
(
    created_by         INTEGER       NOT NULL,
    last_modified_by   INTEGER,
    shop_id            INTEGER       NOT NULL
        PRIMARY KEY
        REFERENCES shop
            ON DELETE CASCADE,
    created_date       TIMESTAMP(6)  NOT NULL,
    last_modified_date TIMESTAMP(6),
    category           VARCHAR(40),
    name               VARCHAR(40)   NOT NULL
        UNIQUE,
    description        VARCHAR(1000) NOT NULL,
    email              VARCHAR(255),
    location           VARCHAR(255),
    phone              VARCHAR(255)
);