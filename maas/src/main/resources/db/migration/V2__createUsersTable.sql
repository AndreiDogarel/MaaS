CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);