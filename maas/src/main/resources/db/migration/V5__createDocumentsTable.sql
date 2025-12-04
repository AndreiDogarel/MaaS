CREATE TABLE files (
   id SERIAL PRIMARY KEY,

   parent_id INTEGER NOT NULL,
   CONSTRAINT fk_files_user
       FOREIGN KEY (parent_id)
           REFERENCES users(id)
           ON DELETE CASCADE,

   cd_name VARCHAR(255),
   cd BYTEA,

   pc_name VARCHAR(255),
   pc BYTEA,

   doc3_name VARCHAR(255),
   doc3_content_type VARCHAR(255),
   doc3_data BYTEA,

   upload_date TIMESTAMP
);