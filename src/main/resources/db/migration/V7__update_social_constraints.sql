ALTER TABLE social
    DROP CONSTRAINT IF EXISTS social_link_key;

UPDATE social SET link = '<LINK>'
WHERE link IS NULL;

ALTER TABLE social
    ALTER COLUMN platform SET NOT NULL,
    ALTER COLUMN link SET NOT NULL,
    ADD CONSTRAINT social_platform_shop_unique UNIQUE (platform, shop_id);