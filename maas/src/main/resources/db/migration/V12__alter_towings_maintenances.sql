ALTER TABLE towings
DROP CONSTRAINT towings_vehicle_id_fkey;

ALTER TABLE maintenances
DROP CONSTRAINT maintenances_vehicle_id_fkey;

ALTER TABLE maintenances ADD COLUMN IF NOT EXISTS is_inspection BOOLEAN;
ALTER TABLE maintenances ADD COLUMN IF NOT EXISTS next_inspection_date DATE;
ALTER TABLE maintenances ADD COLUMN IF NOT EXISTS mileage BIGINT;
