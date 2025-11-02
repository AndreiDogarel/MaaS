CREATE TABLE USERS (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role INTEGER NOT NULL
);

INSERT INTO USERS (username, password, role) VALUES
('admin', '$2a$10$oBqEC5NavrePWeAIbAkEBezAx6TcmdNQaDUDstBNkQvBJwVYB6ae2', 1),
('user', '$2a$10$GLhC2wnSb2B9zezYTVy07eNVoLIaI8A9i5OzWAaRwmCzYSkfq7dtm', 3);