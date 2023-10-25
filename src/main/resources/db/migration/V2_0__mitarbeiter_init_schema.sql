CREATE TABLE mitarbeiter(
        personalnummer INTEGER NOT NULL PRIMARY KEY,
        vorname VARCHAR(255) NOT NULL,
        nachname VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        beschaeftigungsgrad_id INTEGER NOT NULL,
        FOREIGN KEY (beschaeftigungsgrad_id) REFERENCES beschaeftigungsgrad(id)
);

