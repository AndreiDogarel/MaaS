CREATE TABLE USERS (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL
);

INSERT INTO USERS (username, password, role) VALUES
('admin', 'adminpass', 'ADMIN'),
('user', 'userpass', 'USER');