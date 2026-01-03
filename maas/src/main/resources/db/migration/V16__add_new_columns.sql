ALTER TABLE maintenances ADD COLUMN IF NOT EXISTS is_inspection BOOLEAN;
ALTER TABLE maintenances ADD COLUMN IF NOT EXISTS next_inspection_date DATE;
ALTER TABLE maintenances ADD COLUMN IF NOT EXISTS mileage BIGINT;