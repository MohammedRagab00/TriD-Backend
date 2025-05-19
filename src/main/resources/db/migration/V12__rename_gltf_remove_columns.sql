ALTER TABLE model_asset
    RENAME COLUMN gltf TO glb;

ALTER TABLE model_asset
    DROP COLUMN bin,
    DROP COLUMN icon,
    DROP COLUMN texture;

ALTER TABLE shop
    ADD COLUMN logo VARCHAR(100);

CREATE TABLE photo
(
    id                 SERIAL PRIMARY KEY,
    url                VARCHAR(100) NOT NULL,
    shop_id            INTEGER
        REFERENCES shop
            ON DELETE CASCADE,
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP,
    created_by         INTEGER      NOT NULL,
    last_modified_by   INTEGER
);