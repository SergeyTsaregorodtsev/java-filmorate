MERGE INTO genres (GENRE_ID, NAME) VALUES (1,'Комедия');
MERGE INTO genres (GENRE_ID, NAME) VALUES (2,'Драма');
MERGE INTO genres (GENRE_ID, NAME) VALUES (3,'Мультфильм');
MERGE INTO genres (GENRE_ID, NAME) VALUES (4,'Триллер');
MERGE INTO genres (GENRE_ID, NAME) VALUES (5,'Документальный');
MERGE INTO genres (GENRE_ID, NAME) VALUES (6,'Боевик');

MERGE INTO mpa (MPA_ID, NAME) VALUES (1,'G');
MERGE INTO mpa (MPA_ID, NAME) VALUES (2,'PG');
MERGE INTO mpa (MPA_ID, NAME) VALUES (3,'PG-13');
MERGE INTO mpa (MPA_ID, NAME) VALUES (4,'R');
MERGE INTO mpa (MPA_ID, NAME) VALUES (5,'NC-17');

MERGE INTO friend_statuses (FRIEND_STATUS_ID, STATUS) VALUES (1,'unconfirmed');
MERGE INTO friend_statuses (FRIEND_STATUS_ID, STATUS) VALUES (2,'confirmed');