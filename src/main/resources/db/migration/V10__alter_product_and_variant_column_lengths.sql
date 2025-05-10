ALTER TABLE product
    ALTER COLUMN name TYPE varchar(100),
    ALTER COLUMN sizes TYPE varchar(50),
    ALTER COLUMN colors TYPE varchar(50),
    ALTER COLUMN description TYPE varchar(1000),
    ALTER COLUMN base_price TYPE decimal(10,2);

ALTER TABLE product_variant
    ALTER COLUMN color TYPE varchar(50),
    ALTER COLUMN price TYPE decimal(10,2);