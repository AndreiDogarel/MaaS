CREATE TABLE rentals (
                         id              BIGSERIAL PRIMARY KEY,
                         vehicle_id      BIGINT      NOT NULL,
                         user_id         BIGINT      NOT NULL,
                         start_date      TIMESTAMP   NOT NULL,
                         end_date        TIMESTAMP,
                         status          VARCHAR(32) NOT NULL,
                         odometer_start  BIGINT,
                         odometer_end    BIGINT,
                         total_price     NUMERIC(10,2),
                         created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),

                         CONSTRAINT fk_rentals_vehicle
                             FOREIGN KEY (vehicle_id)
                                 REFERENCES vehicles (id),

                         CONSTRAINT fk_rentals_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users (id)
);

CREATE INDEX ix_rentals_vehicle_id ON rentals(vehicle_id);
CREATE INDEX ix_rentals_user_id    ON rentals(user_id);
CREATE INDEX ix_rentals_start_date ON rentals(start_date);
