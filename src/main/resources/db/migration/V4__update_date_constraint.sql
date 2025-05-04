UPDATE users
SET date_of_birth = '1900-01-01'
WHERE date_of_birth IS NULL;

ALTER TABLE users
ALTER COLUMN date_of_birth SET NOT NULL;