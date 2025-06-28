ALTER TABLE users
    ALTER COLUMN date_of_birth DROP NOT NULL,
    DROP CONSTRAINT chk_minimum_age,
    ADD CONSTRAINT chk_minimum_age_when_provided
        CHECK (date_of_birth IS NULL OR date_of_birth <= CURRENT_DATE - INTERVAL '13 years');