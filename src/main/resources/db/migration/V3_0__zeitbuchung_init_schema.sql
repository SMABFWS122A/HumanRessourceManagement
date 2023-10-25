CREATE SEQUENCE IF NOT EXISTS zeitbuchung_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE zeitbuchung(
        id INTEGER NOT NULL PRIMARY KEY,
        uhrzeit TIME NOT NULL,
        datum DATE NOT NULL,
        buchungsart VARCHAR(255) NOT NULL,
        personalnummer INTEGER NOT NULL,
        FOREIGN KEY (personalnummer) REFERENCES mitarbeiter(personalnummer)
);

