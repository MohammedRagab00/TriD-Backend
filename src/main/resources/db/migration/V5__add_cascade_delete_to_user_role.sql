ALTER TABLE user_role
    DROP CONSTRAINT user_foreign_key;

ALTER TABLE user_role
    ADD CONSTRAINT user_foreign_key
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;