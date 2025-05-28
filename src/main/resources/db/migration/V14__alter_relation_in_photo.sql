ALTER TABLE photo
    RENAME COLUMN shop_id TO model_id;

ALTER TABLE photo
    DROP CONSTRAINT photo_shop_id_fkey;

UPDATE photo
SET model_id = s.model_asset_id
FROM shop s
WHERE photo.model_id = s.id;

ALTER TABLE photo
    ADD CONSTRAINT model_id_fk FOREIGN KEY (model_id)
        REFERENCES model_asset (id)
        ON DELETE CASCADE;