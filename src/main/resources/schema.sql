CREATE TABLE IF NOT EXISTS USERS(
            user_id int PRIMARY KEY AUTO_INCREMENT,
            email varchar(255) NOT NULL,
            login varchar(255) NOT NULL,
            name varchar(255) NOT NULL,
            birthday date
);
CREATE TABLE IF NOT EXISTS MPA(
            mpa_id int PRIMARY KEY AUTO_INCREMENT,
            name varchar(255) UNIQUE
);
CREATE TABLE IF NOT EXISTS FILMS(
            film_id int PRIMARY KEY AUTO_INCREMENT,
            name varchar(255) NOT NULL,
            description varchar(255),
            release_date date,
            duration int,
            mpa_id int references MPA(mpa_id)
);
CREATE TABLE IF NOT EXISTS GENRES(
            genre_id int PRIMARY KEY AUTO_INCREMENT,
            name varchar(255) UNIQUE
);
CREATE TABLE IF NOT EXISTS FILM_GENRES(
            film_id int references FILMS(film_id),
            genre_id int references GENRES(genre_id),
            PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS FRIEND_STATUSES(
            friend_status_id int PRIMARY KEY AUTO_INCREMENT,
            status varchar(255) UNIQUE
);
CREATE TABLE IF NOT EXISTS FILM_LIKES(
            film_id int references FILMS(film_id),
            user_id int references USERS(user_id),
            PRIMARY KEY (film_id, user_id)
);
CREATE TABLE IF NOT EXISTS FRIENDS(
            user_id int references USERS(user_id),
            friend_id int references USERS(user_id),
            friend_status_id int references FRIEND_STATUSES(friend_status_id),
            PRIMARY KEY (user_id, friend_id)
);