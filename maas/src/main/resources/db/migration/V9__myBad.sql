ALTER TABLE users
    drop column upload_date_pc ;

ALTER TABLE files
    ADD COLUMN upload_date_pc TIMESTAMP;