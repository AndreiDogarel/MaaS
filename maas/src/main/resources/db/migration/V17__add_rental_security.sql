ALTER TABLE rentals
DROP CONSTRAINT IF EXISTS chk_rental_status;

ALTER TABLE rentals
    ADD CONSTRAINT chk_rental_status
        CHECK (status IN ('PENDING', 'ACTIVE', 'COMPLETED', 'CANCELLED'));
