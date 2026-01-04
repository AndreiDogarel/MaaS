ALTER TABLE MAINTENANCES ADD CONSTRAINT fk_maintenance_vehicle
    FOREIGN KEY (vehicle_id)
        REFERENCES vehicles(id) ON DELETE CASCADE;
ALTER TABLE TOWINGS ADD CONSTRAINT fk_towing_vehicles
    FOREIGN KEY (vehicle_id)
        REFERENCES vehicles(id) ON DELETE CASCADE;