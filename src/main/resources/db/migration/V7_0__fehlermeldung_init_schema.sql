CREATE SEQUENCE IF NOT EXISTS fehlermeldung_id_seq START WITH 1 INCREMENT BY 1;


CREATE TABLE fehlermeldung(
                      id INTEGER NOT NULL PRIMARY KEY,
                      fehlermeldung VARCHAR(255),
                      personalnummer INTEGER NOT NULL,
                      zeitstempel TIMESTAMP WITH TIME ZONE NOT NULL,
                      FOREIGN KEY (personalnummer) REFERENCES mitarbeiter(personalnummer)
);

