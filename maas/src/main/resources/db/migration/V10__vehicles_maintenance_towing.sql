CREATE TABLE IF NOT EXISTS maintenances (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    date DATE NOT NULL,
    type VARCHAR(128) NOT NULL,
    description VARCHAR(256) NOT NULL,
    cost NUMERIC(10, 2) NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS towings (
    id BIGSERIAL PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    date DATE NOT NULL,
    location VARCHAR(128) NOT NULL,
    reason VARCHAR(256) NOT NULL,
    duration INT NOT NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE CASCADE
);

-- Seed Maintenances
INSERT INTO maintenances (vehicle_id, date, type, description, cost) VALUES
((SELECT id FROM vehicles WHERE registration_number = 'QWE-2023'), '2023-01-15', 'Oil Change', 'Regular oil change', 50.00),
((SELECT id FROM vehicles WHERE registration_number = 'FGH-5555'), '2023-03-20', 'Tire Rotation', 'Rotated all four tires', 30.00),
((SELECT id FROM vehicles WHERE registration_number = 'XYZ-7890'), '2023-02-10', 'Brake Inspection', 'Checked brake pads and rotors', 40.00);

-- Seed Towings
INSERT INTO towings (vehicle_id, date, location, reason, duration) VALUES
((SELECT id FROM vehicles WHERE registration_number = 'JKL-9876'), '2023-04-05', 'Highway 101', 'Engine failure', 2);
