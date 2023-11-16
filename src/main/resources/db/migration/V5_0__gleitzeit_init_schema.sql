CREATE SEQUENCE IF NOT EXISTS gleitzeit_id_seq START WITH 6 INCREMENT BY 1;


CREATE TABLE gleitzeit(
                      id INTEGER NOT NULL PRIMARY KEY,
                      gleitzeitsaldo INTEGER NOT NULL,
                      datum DATE NOT NULL,
                      zeitstempel TIMESTAMP WITH TIME ZONE NOT NULL,
                      personalnummer INTEGER NOT NULL,
                      FOREIGN KEY (personalnummer) REFERENCES mitarbeiter(personalnummer)
);

