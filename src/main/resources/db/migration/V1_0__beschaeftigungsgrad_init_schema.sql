CREATE TABLE beschaeftigungsgrad(
  id INTEGER NOT NULL Primary KEY,
  bezeichnung VARCHAR(255) NOT NULL,
  wochenstunden DOUBLE NOT NULL,
  urlaubsanspruch INTEGER NOT NULL,
  beginn_arbeitszeitfenster TIME NOT NULL,
  ende_arbeitszeitfenster TIME NOT NULL
);