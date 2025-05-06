ALTER TABLE social
    DROP CONSTRAINT IF EXISTS social_link_key;

UPDATE social SET link = '<LINK>'
WHERE link IS NULL;

ALTER TABLE social
    ALTER COLUMN platform SET NOT NULL,
    ALTER COLUMN link SET NOT NULL,
    DROP CONSTRAINT IF EXISTS social_shop_fk,
    ADD CONSTRAINT social_platform_shop_unique UNIQUE (platform, shop_id),
    ADD CONSTRAINT social_shop_fk FOREIGN KEY (shop_id)
        REFERENCES shop_detail(id) ON DELETE CASCADE;