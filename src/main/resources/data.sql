MERGE INTO GENRES (GENRE_ID, NAME) VALUES (1,'Комедия');
MERGE INTO GENRES (GENRE_ID, NAME) VALUES (2,'Драма');
MERGE INTO GENRES (GENRE_ID, NAME) VALUES (3,'Мультфильм');
MERGE INTO GENRES (GENRE_ID, NAME) VALUES (4,'Триллер');
MERGE INTO GENRES (GENRE_ID, NAME) VALUES (5,'Документальный');
MERGE INTO GENRES (GENRE_ID, NAME) VALUES (6,'Боевик');

MERGE INTO MPA (MPA_ID, NAME) VALUES (1,'G');
MERGE INTO MPA (MPA_ID, NAME) VALUES (2,'PG');
MERGE INTO MPA (MPA_ID, NAME) VALUES (3,'PG-13');
MERGE INTO MPA (MPA_ID, NAME) VALUES (4,'R');
MERGE INTO MPA (MPA_ID, NAME) VALUES (5,'NC-17');

MERGE INTO FRIEND_STATUSES (FRIEND_STATUS_ID, STATUS) VALUES (1,'unconfirmed');
MERGE INTO FRIEND_STATUSES (FRIEND_STATUS_ID, STATUS) VALUES (2,'confirmed');