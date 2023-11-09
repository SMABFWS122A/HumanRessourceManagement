CREATE SEQUENCE IF NOT EXISTS urlaubsbuchung_id_seq START WITH 7 INCREMENT BY 1;


CREATE TABLE urlaubsbuchung(
                          id INTEGER NOT NULL PRIMARY KEY,
                          von_datum DATE NOT NULL,
                          bis_datum DATE NOT NULL,
                          buchungsart varchar(255) NOT NULL,
                          personalnummer INTEGER NOT NULL,
                          FOREIGN KEY (personalnummer) REFERENCES mitarbeiter(personalnummer)
);
