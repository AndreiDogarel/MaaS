ALTER TABLE USERS ADD CONSTRAINT fk_rental_user
    FOREIGN KEY (role)
        REFERENCES roles(id);