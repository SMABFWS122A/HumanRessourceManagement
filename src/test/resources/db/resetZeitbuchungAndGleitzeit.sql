DELETE FROM zeitbuchung;
ALTER SEQUENCE zeitbuchung_id_seq RESTART;
DELETE FROM gleitzeit WHERE ID > 5;
ALTER SEQUENCE gleitzeit_id_seq RESTART WITH 6;
DELETE FROM urlaubsbuchung WHERE id = 7;
ALTER SEQUENCE urlaubsbuchung_id_seq RESTART WITH 7;
DELETE FROM fehlermeldung;
ALTER SEQUENCE fehlermeldung_id_seq RESTART;


