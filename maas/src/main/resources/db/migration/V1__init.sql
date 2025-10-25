CREATE TABLE masina (
    id SERIAL PRIMARY KEY,
    categorie VARCHAR(50) NOT NULL,
    culoare VARCHAR(30) NOT NULL,
    numar_locuri INT CHECK (numar_locuri > 0),
    pret DECIMAL(10,2) CHECK (pret >= 0)
);