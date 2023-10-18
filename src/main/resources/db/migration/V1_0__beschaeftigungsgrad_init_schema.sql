CREATE TABLE beschaeftigungsgrad(
  id INTEGER NOT NULL,
  bezeichnung VARCHAR(255) NOT NULL,
  wochenstunden DOUBLE NOT NULL,
  beginn_arbeitszeitfenster TIME NOT NULL,
  ende_arbeitszeitfenster TIME NOT NULL,
  PRIMARY KEY (id)
);