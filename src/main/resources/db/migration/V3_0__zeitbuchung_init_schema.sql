CREATE TABLE zeitbuchung(
        id INTEGER NOT NULL PRIMARY KEY,
        uhrzeit TIME NOT NULL,
        datum DATE NOT NULL,
        buchungsart VARCHAR(255) NOT NULL,
        personalnummer INTEGER NOT NULL,
        FOREIGN KEY (personalnummer) REFERENCES mitarbeiter(personalnummer)
);

