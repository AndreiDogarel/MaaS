ALTER TABLE files
RENAME COLUMN upload_date TO upload_date_cd;

ALTER TABLE users
ADD COLUMN upload_date_pc TIMESTAMP;