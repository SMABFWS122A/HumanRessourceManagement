CREATE TABLE login(
                            email VARCHAR(255) NOT NULL PRIMARY KEY,
                            passwort VARCHAR(255) NOT NULL,
                            admin BIT NOT NULL,
                            personalnummer INTEGER NOT NULL,
                            FOREIGN KEY (personalnummer) REFERENCES mitarbeiter(personalnummer)
);

