CREATE TABLE review
(
    id                 INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    user_id            INT NOT NULL,
    product_id         INT NOT NULL,
    rating             SMALLINT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment            VARCHAR(200),
    created_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);

CREATE TABLE addresses
(
    id           INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    user_id      INT           NOT NULL,
    address      VARCHAR(200),
    latitude     DECIMAL(9, 6) NOT NULL,
    longitude    DECIMAL(9, 6) NOT NULL,
    phone_number VARCHAR(15),
    landmark     VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id, longitude, latitude)
);