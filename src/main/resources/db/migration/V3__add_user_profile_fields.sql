ALTER TABLE users
    ADD COLUMN photo VARCHAR(255),
    ADD COLUMN gender SMALLINT;

-- Add check constraints
ALTER TABLE users
    ADD CONSTRAINT chk_minimum_age
    CHECK (date_of_birth <= CURRENT_DATE - INTERVAL '13 years'),
    ADD CONSTRAINT chk_gender_values
    CHECK (gender BETWEEN 0 AND 2);