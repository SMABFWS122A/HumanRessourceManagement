DELETE FROM zeitbuchung;
ALTER SEQUENCE zeitbuchung_id_seq RESTART;
DELETE FROM gleitzeit WHERE ID > 5;
ALTER SEQUENCE gleitzeit_id_seq RESTART WITH 6;
