CREATE TABLE rental_contract (
         id BIGSERIAL PRIMARY KEY,
         start_date DATE NOT NULL,
         end_date DATE NOT NULL,
         total_price DOUBLE PRECISION,
         created_at TIMESTAMP,

         user_id BIGINT,
         vehicle_id BIGINT,
         operator_id BIGINT,

         CONSTRAINT fk_rental_user
             FOREIGN KEY (user_id)
                 REFERENCES users(id),

         CONSTRAINT fk_rental_vehicle
             FOREIGN KEY (vehicle_id)
                 REFERENCES vehicles(id),

         CONSTRAINT fk_rental_operator
             FOREIGN KEY (operator_id)
                 REFERENCES users(id)
);